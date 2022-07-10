package com.marioioannou.cryptocurrencyapp.coin_data.model.coin_search

import java.io.Serializable

data class Coin(
    val availableSupply: Double?,
    val exp: List<String>?,
    val icon: String?,
    val id: String?,
    val marketCap: Double?,
    val name: String?,
    val price: Double?,
    val priceBtc: Double?,
    val priceChange1d: Double?,
    val priceChange1h: Double?,
    val priceChange1w: Double?,
    val rank: Int?,
    val symbol: String?,
    val totalSupply: Double?,
    val twitterUrl: String?,
    val volume: Double?,
    val websiteUrl: String?
): Serializable