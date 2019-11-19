package com.example.learnplaid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learnplaid.data.GankRepo
import com.example.learnplaid.pages.HomeViewModel

class ViewModelFactory: ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val vm =  when(modelClass) {
            HomeViewModel::class.java -> HomeViewModel(GankRepo.getInstance())
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
        return  vm as T
    }

}