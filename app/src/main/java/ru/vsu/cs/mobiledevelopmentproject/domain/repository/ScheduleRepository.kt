package ru.vsu.cs.mobiledevelopmentproject.domain.repository

import ru.vsu.cs.mobiledevelopmentproject.domain.model.Lesson
import ru.vsu.cs.mobiledevelopmentproject.domain.model.ScheduleFile

interface ScheduleRepository {
    suspend fun getScheduleFile(): ScheduleFile
    suspend fun downloadSchedule(): ByteArray

    fun getLessons(): List<Lesson>
}
