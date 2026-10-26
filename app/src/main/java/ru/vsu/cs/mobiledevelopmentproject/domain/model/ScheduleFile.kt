package ru.vsu.cs.mobiledevelopmentproject.domain.model

import java.time.OffsetDateTime

data class ScheduleFile(
    val name: String,
    val updatedAt: OffsetDateTime,
)
