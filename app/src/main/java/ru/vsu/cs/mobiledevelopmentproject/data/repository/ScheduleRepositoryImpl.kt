package ru.vsu.cs.mobiledevelopmentproject.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.vsu.cs.mobiledevelopmentproject.data.local.SampleSchedule
import ru.vsu.cs.mobiledevelopmentproject.data.mapper.toDomain
import ru.vsu.cs.mobiledevelopmentproject.data.remote.YandexDiskApi
import ru.vsu.cs.mobiledevelopmentproject.domain.model.Lesson
import ru.vsu.cs.mobiledevelopmentproject.domain.model.ScheduleFile
import ru.vsu.cs.mobiledevelopmentproject.domain.repository.ScheduleRepository

class ScheduleRepositoryImpl(private val api: YandexDiskApi) : ScheduleRepository {
    companion object {
        private const val SCHEDULE_URL = "https://disk.yandex.ru/i/cNAnuN26XGzFGA"
    }

    override suspend fun getScheduleFile(): ScheduleFile {
        return api.getPublicResource(SCHEDULE_URL).toDomain()
    }

    override suspend fun downloadSchedule(): ByteArray {
        val downloadLink = api.getDownloadLink(SCHEDULE_URL)
        return withContext(Dispatchers.IO) {
            api.downloadSchedule(downloadLink.href).use {
                body -> body.bytes()
            }
        }
    }

    override fun getLessons(): List<Lesson> {
        return SampleSchedule.lessons
    }
}
