package com.diegor.productsearch.di

import com.diegor.productsearch.BuildConfig
import com.diegor.productsearch.data.api.ProductsService
import com.diegor.productsearch.data.api.ProductsService.Companion.BASE_URL
import com.diegor.productsearch.util.DateTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideGson(): Gson {
        val builder = GsonBuilder()
            .registerTypeAdapter(
                Date::class.java,
                DateTypeAdapter()
            )

        return builder.create()
    }

    private fun getLogginInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(getLogginInterceptor())
        }.build()
    }

    @Provides
    @Singleton
    fun provideProductsService(
        gson: Gson,
        okHttpClient: OkHttpClient
    ): ProductsService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ProductsService::class.java)
    }
}