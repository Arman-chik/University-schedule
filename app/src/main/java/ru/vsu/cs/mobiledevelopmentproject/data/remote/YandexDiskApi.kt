package ru.vsu.cs.mobiledevelopmentproject.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url
import ru.vsu.cs.mobiledevelopmentproject.data.remote.dto.DownloadLinkDto
import ru.vsu.cs.mobiledevelopmentproject.data.remote.dto.PublicResourceDto

interface YandexDiskApi {

    @GET("v1/disk/public/resources")
    suspend fun getPublicResource(
        @Query("public_key") publicKey: String
    ): PublicResourceDto

    @GET("v1/disk/public/resources/download")
    suspend fun getDownloadLink(
        @Query("public_key") publicKey: String
    ): DownloadLinkDto

    @Streaming
    @GET
    suspend fun downloadSchedule(@Url url: String): ResponseBody
}