package ru.vsu.cs.mobiledevelopmentproject.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PublicResourceDto(
    val name: String,
    val type: String,
    val modified: String,
    val size: Long? = null,
    @SerialName("mime_type")
    val mimeType: String? = null,
    val md5: String? = null
)
