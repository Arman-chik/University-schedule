package ru.vsu.cs.mobiledevelopmentproject.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DownloadLinkDto(
    val href: String
)
