package com.marioioannou.cryptocurrencyapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marioioannou.cryptocurrencyapp.coin_data.repository.CoinRepository

class MainViewModelFactory(
    private val application: Application,
    private val repository: CoinRepository
    ): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(application,repository) as T
    }
}