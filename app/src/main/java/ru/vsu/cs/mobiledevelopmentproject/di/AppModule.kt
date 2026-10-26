package ru.vsu.cs.mobiledevelopmentproject.di

import ru.vsu.cs.mobiledevelopmentproject.data.repository.ScheduleRepositoryImpl
import ru.vsu.cs.mobiledevelopmentproject.domain.repository.ScheduleRepository

object AppModule {
    val scheduleRepository: ScheduleRepository by lazy {
        ScheduleRepositoryImpl(NetworkModule.yandexDiskApi)
    }
}