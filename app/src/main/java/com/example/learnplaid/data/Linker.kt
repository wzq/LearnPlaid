package com.example.learnplaid.data

import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Linker {

    private const val BASE_URL = "https://www.wanandroid.com"


    private val logger = HttpLoggingInterceptor(
        HttpLoggingInterceptor.Logger.DEFAULT
    ).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder().addInterceptor(logger).build()


    private val url = BASE_URL.toHttpUrl()
    private val retrofit = Retrofit.Builder().baseUrl(url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val api: Api by lazy { retrofit.create(
        Api::class.java) }

}