package ru.vsu.cs.mobiledevelopmentproject.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.vsu.cs.mobiledevelopmentproject.di.AppModule
import ru.vsu.cs.mobiledevelopmentproject.domain.repository.ScheduleRepository
import java.io.IOException

class ScheduleViewModel(
    private val repository: ScheduleRepository
) : ViewModel() {
    private val _state = MutableStateFlow<ScheduleUiState>(ScheduleUiState.Loading)
    val state: StateFlow<ScheduleUiState> = _state

    init {
        loadSchedule()
    }

    fun loadSchedule() {
        viewModelScope.launch {
            _state.value = ScheduleUiState.Loading
            try {
                val file = repository.getScheduleFile()
                _state.value = ScheduleUiState.Success(file = file, lessons = repository.getLessons())
            } catch (e: Exception) {
                _state.value = ScheduleUiState.Error(errorMessage(e))
            }
        }
    }

    private fun errorMessage(error: Exception): String = when (error) {
        is IOException -> "Нет подключения к интернету"
        is HttpException -> {
            when (error.code()) {
                404 -> "Файл или страница не найдены"
                500, 503 -> "Сервер временно недоступен. Попробуйте позже"
                403, 401 -> "Доступ ограничен. Требуется авторизация"
                else -> "Ошибка сервера (Код: ${error.code()})"
            }
        }
        else -> "Что-то пошло не так"
    }


    fun downloadSchedule() {
        val current = _state.value as? ScheduleUiState.Success ?: return

        if (current.download is DownloadState.InProgress) return

        viewModelScope.launch {
            _state.value = current.copy(download = DownloadState.InProgress)
            try {
                val bytes = repository.downloadSchedule()
                _state.value = current.copy(download = DownloadState.Done(bytes.size))
            } catch (e: Exception) {
                _state.value = current.copy(download = DownloadState.Failed(errorMessage(e)))
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ScheduleViewModel(AppModule.scheduleRepository)
            }
        }
    }
}