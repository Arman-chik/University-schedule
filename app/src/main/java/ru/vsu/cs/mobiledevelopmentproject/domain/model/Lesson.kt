package ru.vsu.cs.mobiledevelopmentproject.domain.model

data class Lesson(
    val day: String,
    val time: String,
    val title: String,
    val details: String?,
    val week: WeekType
)
