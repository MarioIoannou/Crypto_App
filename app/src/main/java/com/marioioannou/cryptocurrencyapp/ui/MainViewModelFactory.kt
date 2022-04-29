package com.marioioannou.cryptocurrencyapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marioioannou.cryptocurrencyapp.coin_data.repository.CoinRepository

class MainViewModelFactory(private val coin_repository: CoinRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(coin_repository) as T
    }
}