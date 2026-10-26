package ru.vsu.cs.mobiledevelopmentproject

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import ru.vsu.cs.mobiledevelopmentproject.ui.schedule.DownloadState
import ru.vsu.cs.mobiledevelopmentproject.ui.schedule.ScheduleUiState
import ru.vsu.cs.mobiledevelopmentproject.ui.schedule.ScheduleViewModel
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class ScheduleViewModelTest {

    private lateinit var repository: FakeScheduleRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = FakeScheduleRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `при успешной загрузке состояние Success с файлом и парами`() {
        val viewModel = ScheduleViewModel(repository)

        val state = viewModel.state.value
        assertTrue(state is ScheduleUiState.Success)
        state as ScheduleUiState.Success
        assertEquals(repository.file, state.file)
        assertEquals(repository.sampleLessons, state.lessons)
    }

    @Test
    fun `без интернета показывается понятная ошибка`() {
        repository.fileError = IOException("Unable to resolve host")

        val viewModel = ScheduleViewModel(repository)

        assertEquals(
            ScheduleUiState.Error("Нет подключения к интернету"),
            viewModel.state.value,
        )
    }

    @Test
    fun `при ответе 404 показывается ошибка не найдено`() {
        repository.fileError = HttpException(Response.error<Any>(404, "".toResponseBody()))

        val viewModel = ScheduleViewModel(repository)

        assertEquals(
            ScheduleUiState.Error("Файл или страница не найдены"),
            viewModel.state.value,
        )
    }

    @Test
    fun `повторная загрузка после ошибки приводит к Success`() {
        repository.fileError = IOException()
        val viewModel = ScheduleViewModel(repository)

        repository.fileError = null
        viewModel.loadSchedule()

        assertTrue(viewModel.state.value is ScheduleUiState.Success)
    }

    @Test
    fun `успешное скачивание сохраняет размер файла`() {
        val viewModel = ScheduleViewModel(repository)

        viewModel.downloadSchedule()

        val state = viewModel.state.value as ScheduleUiState.Success
        assertEquals(DownloadState.Done(135714), state.download)
    }

    @Test
    fun `ошибка скачивания не ломает экран, а показывается в DownloadState`() {
        repository.downloadError = IOException()
        val viewModel = ScheduleViewModel(repository)

        viewModel.downloadSchedule()

        val state = viewModel.state.value as ScheduleUiState.Success
        assertEquals(DownloadState.Failed("Нет подключения к интернету"), state.download)
    }
}