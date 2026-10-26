package ru.vsu.cs.mobiledevelopmentproject.ui.schedule

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val formatter = DateTimeFormatter
    .ofPattern("d MMMM yyyy, HH:mm", Locale.forLanguageTag("ru"))

fun formatDate(date: OffsetDateTime): String =
    "Обновлено: " + date.atZoneSameInstant(ZoneId.systemDefault())
        .format(formatter)