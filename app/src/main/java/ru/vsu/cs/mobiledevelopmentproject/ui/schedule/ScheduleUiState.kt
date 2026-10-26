package ru.vsu.cs.mobiledevelopmentproject.ui.schedule

import ru.vsu.cs.mobiledevelopmentproject.domain.model.Lesson
import ru.vsu.cs.mobiledevelopmentproject.domain.model.ScheduleFile

sealed interface ScheduleUiState {
    data object Loading : ScheduleUiState
    data class Success(val file: ScheduleFile,
                       val lessons: List<Lesson>,
                       val download: DownloadState = DownloadState.Idle) : ScheduleUiState
    data class Error(val message: String) : ScheduleUiState
}

sealed interface DownloadState {
    data object Idle : DownloadState
    data object InProgress : DownloadState
    data class Done(val sizeBytes: Int) : DownloadState
    data class Failed(val message: String) : DownloadState
}
