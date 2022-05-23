package com.marioioannou.cryptocurrencyapp.coin_data.repository

import com.marioioannou.cryptocurrencyapp.coin_data.api.CoinApiService
import com.marioioannou.cryptocurrencyapp.coin_data.model.crypto.CryptoCoin
import com.marioioannou.cryptocurrencyapp.coin_data.model.cryptosearch.CoinSearch
import com.marioioannou.cryptocurrencyapp.coin_data.model.trending_coins.TrendingCoins
import com.marioioannou.newsapp.news_data.model.News
import retrofit2.Response

class CoinRepository {
    //suspend fun getCoinRepository(currency: String) = CoinApiService.api.getCoinsData(currency)
    suspend fun getTrendingCoinsRepository(): Response<TrendingCoins> {
        return CoinApiService.coinApi.getTrendingCoins()
    }

    suspend fun getCoinRepository(currency: String,page: Int): Response<MutableList<CryptoCoin>> {
        return CoinApiService.coinApi.getCoinsData(currency,page)
    }

    suspend fun getSearchedCoinsRepository(searchQuery: String) : Response<CoinSearch>{
        return CoinApiService.coinApi.getSearchedCoins(searchQuery)
    }

    suspend fun getNewsRepository(countryCode: String, newsPages: Int,categoryNews:String) : Response<News>{
        return CoinApiService.newsApi.getNews(countryCode,newsPages,categoryNews)
    }
}