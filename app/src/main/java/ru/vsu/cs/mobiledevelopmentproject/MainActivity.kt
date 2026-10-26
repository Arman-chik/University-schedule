package ru.vsu.cs.mobiledevelopmentproject

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.vsu.cs.mobiledevelopmentproject.databinding.ActivityMainBinding
import ru.vsu.cs.mobiledevelopmentproject.ui.schedule.DownloadState
import ru.vsu.cs.mobiledevelopmentproject.ui.schedule.LessonAdapter
import ru.vsu.cs.mobiledevelopmentproject.ui.schedule.ScheduleUiState
import ru.vsu.cs.mobiledevelopmentproject.ui.schedule.ScheduleViewModel
import ru.vsu.cs.mobiledevelopmentproject.ui.schedule.formatDate

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: ScheduleViewModel by viewModels{
        ScheduleViewModel.Factory
    }

    private val lessonAdapter = LessonAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lessonsList.layoutManager = LinearLayoutManager(this)
        binding.lessonsList.adapter = lessonAdapter

        val baseBottom = binding.lessonsList.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(binding.lessonsList) { view, insets ->
            val navBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.updatePadding(bottom = baseBottom + navBar.bottom)
            insets
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    render(state)
                }
            }
        }

        binding.downloadButton.setOnClickListener { viewModel.downloadSchedule() }
        binding.retryButton.setOnClickListener { viewModel.loadSchedule() }



        val basePadding = binding.header.paddingTop
        ViewCompat.setOnApplyWindowInsetsListener(binding.header) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = basePadding + statusBar.top)
            insets
        }

    }


    private fun render(state: ScheduleUiState) {
        binding.progress.isVisible = state is ScheduleUiState.Loading
        binding.errorGroup.isVisible = state is ScheduleUiState.Error
        binding.contentGroup.isVisible = state is ScheduleUiState.Success

        when ( state) {
            is ScheduleUiState.Loading -> Unit
            is ScheduleUiState.Error -> binding.errorText.text = state.message
            is ScheduleUiState.Success -> {
                binding.fileName.text = state.file.name
                binding.fileUpdated.text = formatDate(state.file.updatedAt)
                lessonAdapter.submitList(state.lessons)
                renderDownload(state.download)
            }
        }
    }


    private fun renderDownload(download: DownloadState) {
        binding.downloadButton.isEnabled = download !is DownloadState.InProgress
        binding.downloadButton.text = when (download) {
            is DownloadState.Idle -> "Скачать расписание"
            is DownloadState.InProgress -> "Скачиваю"
            is DownloadState.Done-> "Скачать заново"
            is DownloadState.Failed -> "Повторить скачивание"
        }

        binding.downloadStatus.isVisible = download is DownloadState.Done
                || download is DownloadState.Failed
        binding.downloadStatus.text = when (download) {
            is DownloadState.Done -> "Скачано: ${download.sizeBytes} байт"
            is DownloadState.Failed -> download.message
            is DownloadState.Idle -> ""
            is DownloadState.InProgress -> ""
        }

    }

}
