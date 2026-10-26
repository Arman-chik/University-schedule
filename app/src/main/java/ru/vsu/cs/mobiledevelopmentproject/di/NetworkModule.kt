package ru.vsu.cs.mobiledevelopmentproject.di

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.vsu.cs.mobiledevelopmentproject.data.remote.YandexDiskApi

object NetworkModule {

    private const val BASE_URL = "https://cloud-api.yandex.net/"

    private val json = Json {
        ignoreUnknownKeys = true
    }


    private val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    val yandexDiskApi: YandexDiskApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(YandexDiskApi::class.java)
    }
}
