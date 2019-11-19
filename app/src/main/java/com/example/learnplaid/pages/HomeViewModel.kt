package com.example.learnplaid.pages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnplaid.data.GankRepo
import com.example.learnplaid.model.Article
import com.example.learnplaid.model.result.ArticleResult
import com.example.learnplaid.model.result.DataResult
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: GankRepo) : ViewModel() {
    private val _sources = MutableLiveData<DataResult<List<Article>>>()
    val sources: LiveData<DataResult<List<Article>>>
        get() = _sources

    init {
        fetchDailyInfo()
    }

    private fun fetchDailyInfo() {
        viewModelScope.launch {
            _sources.postValue(repo.getDailyInfo())
        }
    }
}