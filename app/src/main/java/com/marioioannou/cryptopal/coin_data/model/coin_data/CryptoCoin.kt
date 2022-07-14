package com.marioioannou.cryptopal.coin_data.model.coin_data

import java.io.Serializable

data class CryptoCoin(
    val availableSupply: Double?,
    val contractAddress: String?,
    val decimals: Int?,
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
    val redditUrl: String?,
    val symbol: String?,
    val totalSupply: Double?,
    val twitterUrl: String?,
    val volume: Double?,
    val websiteUrl: String?
): Serializable