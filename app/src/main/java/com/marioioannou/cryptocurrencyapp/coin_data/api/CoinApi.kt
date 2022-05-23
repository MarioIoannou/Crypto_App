package com.marioioannou.cryptocurrencyapp.coin_data.api

import com.marioioannou.cryptocurrencyapp.coin_data.model.crypto.CryptoCoin
import com.marioioannou.cryptocurrencyapp.coin_data.model.cryptosearch.CoinSearch
import com.marioioannou.cryptocurrencyapp.coin_data.model.trending_coins.TrendingCoins
import com.marioioannou.cryptocurrencyapp.utils.Constants.Companion.API_KEY
import com.marioioannou.newsapp.news_data.model.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinApi {
    @GET("api/v3/search/trending")
    suspend fun getTrendingCoins():Response<TrendingCoins>

    @GET("api/v3/coins/markets/")
    suspend fun getCoinsData(
        @Query("vs_currency")
        currency: String,
        @Query("page")
        pageNumber: Int
    ): Response<MutableList<CryptoCoin>>

    @GET("api/v3/search")
    suspend fun getSearchedCoins(
        @Query("query")
        currency: String
    ): Response<CoinSearch>

    @GET("v2/top-headlines")
    suspend fun getNews(
        @Query("country")
        countryCode: String = "us",
        @Query("pageSize")
        pages: Int = 100,
        @Query("category")
        categoryNews: String = "business",
        @Query("apiKey")
        apiKey: String = API_KEY,
    ): Response<News>
}