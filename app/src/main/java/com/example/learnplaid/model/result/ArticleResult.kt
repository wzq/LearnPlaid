package com.example.learnplaid.model.result

import com.example.learnplaid.model.Article


/**
 * Created by wzq on 2019-07-12
 *
 */
data class ArticleResult(
        val data: ArticleData
): BaseResult()

data class ArticleData(
    val curPage: Int,
    var datas: MutableList<Article>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)
