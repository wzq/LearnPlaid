package com.example.learnplaid.data

import com.example.learnplaid.model.result.DataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRepo(private val api: Api = Linker.api) {

    suspend fun getDailyInfo() = withContext(Dispatchers.IO) {
        val p1 = api.getLastProjects(0)
        val p2 = api.getLastProjects(1)

        DataResult.Success(p1.data.datas.toMutableList().apply {
            addAll(p2.data.datas)
        })
    }

    companion object {

        @Volatile
        private var INSTANCE: HomeRepo? = null

        fun getInstance(): HomeRepo {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: HomeRepo().also {
                    INSTANCE = it
                }
            }
        }
    }
}