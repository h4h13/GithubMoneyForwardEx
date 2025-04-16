package com.example.github_moneyforward_ex.di

import com.example.github_moneyforward_ex.data.api.GitHubApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Named("baseUrl")
    fun provideBaseUrl(): String {
        return "https://api.github.com/"
    }

    @Provides
    @Named("token")
    fun provideToken(): String {
        return "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"
    }

    @Provides
    @Singleton
    @Named("authInterceptor")
    fun providesAuthInterceptor(
        @Named("token")
        token: String
    ): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder().addHeader("Authorization", "token $token")
                .build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun providesAuthInterceptorOkHttpClient(
        @Named("authInterceptor")
        authInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        @Named("baseUrl") baseUrl: String,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    }

    @Provides
    @Singleton
    fun provideGitHubApiService(
        retrofit: Retrofit
    ): GitHubApiService = retrofit.create(GitHubApiService::class.java)
}