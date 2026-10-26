package ru.vsu.cs.mobiledevelopmentproject.data.mapper

import ru.vsu.cs.mobiledevelopmentproject.data.remote.dto.PublicResourceDto
import ru.vsu.cs.mobiledevelopmentproject.domain.model.ScheduleFile
import java.time.OffsetDateTime

fun PublicResourceDto.toDomain(): ScheduleFile {
    return ScheduleFile(
        name = name,
        updatedAt = OffsetDateTime.parse(modified)
    )
}
