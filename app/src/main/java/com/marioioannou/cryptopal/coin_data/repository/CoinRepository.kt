package com.marioioannou.cryptopal.coin_data.repository

import com.marioioannou.cryptopal.coin_data.api.CoinApiService
import com.marioioannou.cryptopal.coin_data.model.coin_charts.CoinChart
import com.marioioannou.cryptopal.coin_data.model.coin_data.CoinData
import com.marioioannou.cryptopal.coin_data.model.coin_news.CoinNews
import com.marioioannou.cryptopal.coin_data.model.coin_search.CoinSearch
import com.marioioannou.cryptopal.coin_data.model.trending_coins.TrendingCoins
import retrofit2.Response
import java.util.*

class CoinRepository {

    //Network
    suspend fun getTrendingCoins(): Response<TrendingCoins> {
        return CoinApiService.trendingCoinApi.getTrendingCoins()
    }

    suspend fun getCoinData(currency: String, limit: Int, skip: Int): Response<CoinData>{
        return CoinApiService.coinApi.getCoins(currency, limit, skip)
    }

    suspend fun getCoinSearch(coinId: String, currency: String): Response<CoinSearch>{
        return CoinApiService.coinApi.getCoinById(coinId, currency)
    }

    suspend fun getCoinChart(coinId: String, period: String): Response<CoinChart>{
        return CoinApiService.coinApi.getCoinChartsData(coinId, period)
    }

    suspend fun getCoinNews(filter: String, limit: Int, skip: Int): Response<CoinNews>{
        return CoinApiService.coinApi.getCoinNews(filter,limit,skip)
    }
}