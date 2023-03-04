package com.sartimau.openpayfit.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(
    includes = [
        ApiModule::class,
        DataBaseModule::class,
        ServiceModule::class,
        UseCaseModule::class
    ]
)
@InstallIn(SingletonComponent::class)
class AppModule
