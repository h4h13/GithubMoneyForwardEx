package com.example.github_moneyforward_ex.data.api

import com.example.github_moneyforward_ex.data.api.RetrofitClient.BASE_URL
import com.example.github_moneyforward_ex.data.api.RetrofitClient.client
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

object RetrofitClient {

    private const val BASE_URL = "https://api.github.com/"
    private const val TOKEN = "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"

    private val authInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder().addHeader("Authorization", "token $TOKEN")
            .build()
        chain.proceed(request)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    val apiService: GitHubApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(GitHubApiService::class.java)
    }
}




