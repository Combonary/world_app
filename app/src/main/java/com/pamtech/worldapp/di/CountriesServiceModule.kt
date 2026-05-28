package com.pamtech.worldapp.di

import android.content.Context
import com.pamtech.countriesservice.CountriesApi
import com.pamtech.countriesservice.database.getDatabaseBuilder
import com.pamtech.countriesservice.repository.CountriesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CountriesServiceModule {

    @Provides
    @Singleton
    fun provideCountriesApi(@ApplicationContext context: Context): CountriesApi {
        val database = getDatabaseBuilder(context).build()
        return CountriesApi(database)
    }

    @Provides
    @Singleton
    fun provideCountriesRepository(api: CountriesApi): CountriesRepository {
        return api.repository
    }
}
