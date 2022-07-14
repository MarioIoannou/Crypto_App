package com.marioioannou.cryptopal.coin_data.api


import com.marioioannou.cryptopal.coin_data.model.coin_charts.CoinChart
import com.marioioannou.cryptopal.coin_data.model.coin_search.CoinSearch
import com.marioioannou.cryptopal.coin_data.model.coin_data.CoinData
import com.marioioannou.cryptopal.coin_data.model.coin_news.CoinNews
import com.marioioannou.cryptopal.coin_data.model.trending_coins.TrendingCoins
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinApi {
    @GET("api/v3/search/trending")
    suspend fun getTrendingCoins():Response<TrendingCoins>

    @GET("v1/coins")
    suspend fun getCoins(
        @Query("currency") currency: String = "EUR",
        @Query("limit") limit: Int = 1000,
        @Query("skip") skip: Int = 0
    ): Response<CoinData>

    @GET("v1/coins/{coinId}")
    suspend fun getCoinById(
        @Path("coinId") coinId: String,
        @Query("currency") currency: String = "EUR"
    ): Response<CoinSearch>

    @GET("v1/charts")
    suspend fun getCoinChartsData(
        @Query("coinId") coinId: String,
        @Query("period") period: String = "24h"
    ): Response<CoinChart>

    @GET("v1/news/{filter}")
    suspend fun getCoinNews(
        @Path("filter") filter: String = "trending",
        @Query("limit") limit: Int = 20,
        @Query("skip") skip: Int = 0
    ): Response<CoinNews>

}