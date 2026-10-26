package ru.vsu.cs.mobiledevelopmentproject

import ru.vsu.cs.mobiledevelopmentproject.domain.model.Lesson
import ru.vsu.cs.mobiledevelopmentproject.domain.model.ScheduleFile
import ru.vsu.cs.mobiledevelopmentproject.domain.model.WeekType
import ru.vsu.cs.mobiledevelopmentproject.domain.repository.ScheduleRepository
import java.time.OffsetDateTime

/** не настоящий репозиторий: отдаёт заранее заданные данные
 *  или кидает заданную ошибку, без сети */
class FakeScheduleRepository : ScheduleRepository {

    val file = ScheduleFile(name = "Расписание.xlsx", updatedAt = OffsetDateTime.parse("2026-10-16T04:26:51+00:00"))
    val sampleLessons = listOf(Lesson("Пн", "13:25–15:00", "Теория информации", null, WeekType.EVERY))

    var fileError: Exception? = null
    var downloadError: Exception? = null
    var downloadedBytes = ByteArray(135714)

    override suspend fun getScheduleFile(): ScheduleFile {
        fileError?.let { throw it }
        return file
    }

    override suspend fun downloadSchedule(): ByteArray {
        downloadError?.let { throw it }
        return downloadedBytes
    }

    override fun getLessons(): List<Lesson> = sampleLessons
}