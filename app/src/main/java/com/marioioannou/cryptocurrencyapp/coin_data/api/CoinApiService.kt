package com.marioioannou.cryptocurrencyapp.coin_data.api

import com.marioioannou.cryptocurrencyapp.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CoinApiService {
    companion object {
        //Coin
        private val coin_api by lazy {
            val logging = HttpLoggingInterceptor() //Log responses of retrofit
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            Retrofit.Builder()
                .baseUrl(Constants.COIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val coinApi: CoinApi by lazy {
            coin_api.create(CoinApi::class.java)
        }

        //News
        private val news_api by lazy {
            val loggingNews = HttpLoggingInterceptor() //Log responses of retrofit
            loggingNews.setLevel(HttpLoggingInterceptor.Level.BODY)
            val clientNews = OkHttpClient.Builder()
                .addInterceptor(loggingNews)
                .build()

            Retrofit.Builder()
                .baseUrl(Constants.NEWS_DATA_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientNews)
                .build()
        }

        val newsApi: CoinApi by lazy {
            news_api.create(CoinApi::class.java)
        }
    }
}