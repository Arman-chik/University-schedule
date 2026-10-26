package ru.vsu.cs.mobiledevelopmentproject

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.vsu.cs.mobiledevelopmentproject.data.mapper.toDomain
import ru.vsu.cs.mobiledevelopmentproject.data.remote.dto.PublicResourceDto
import java.time.OffsetDateTime
import java.time.ZoneOffset

class ScheduleFileMapperTest {

    @Test
    fun `toDomain переносит имя файла`() {
        val dto = PublicResourceDto(
            name = "Расписание.xlsx",
            type = "file",
            modified = "2026-10-16T04:26:51+00:00",
        )

        val result = dto.toDomain()

        assertEquals("Расписание.xlsx", result.name)
    }

    @Test
    fun `toDomain превращает строку modified в дату`() {
        val dto = PublicResourceDto(
            name = "Расписание.xlsx",
            type = "file",
            modified = "2026-10-16T04:26:51+00:00",
        )

        val result = dto.toDomain()

        val expected = OffsetDateTime.of(2026, 10, 16, 4, 26, 51, 0, ZoneOffset.UTC)
        assertEquals(expected, result.updatedAt)
    }
}