package com.marioioannou.cryptocurrencyapp.coin_data.repository

import com.marioioannou.cryptocurrencyapp.coin_data.api.CoinApiService
import com.marioioannou.cryptocurrencyapp.coin_data.model.crypto.CryptoCoin
import com.marioioannou.newsapp.news_data.model.News
import retrofit2.Response

class CoinRepository {
    //suspend fun getCoinRepository(currency: String) = CoinApiService.api.getCoinsData(currency)
    suspend fun getCoinRepository(currency: String): Response<MutableList<CryptoCoin>> {
        return CoinApiService.coinApi.getCoinsData(currency)
    }

    suspend fun getNewsRepository(countryCode: String, pages: Int,categoryNews:String) : Response<News>{
        return CoinApiService.newsApi.getNews(countryCode,pages,categoryNews)
    }
}