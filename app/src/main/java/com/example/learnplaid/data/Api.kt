package com.example.learnplaid.data

import com.example.learnplaid.model.result.ArticleResult
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {


    @GET("article/listproject/{pageNum}/json")
    suspend fun getLastProjects(@Path("pageNum") pageNum: Int): ArticleResult

}