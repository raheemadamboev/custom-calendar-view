package xyz.teamgravity.customcalendarview.injection.provide

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.teamgravity.customcalendarview.data.repository.MainRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideMainRepository(): MainRepository = MainRepository()
}