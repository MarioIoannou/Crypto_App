package com.marioioannou.cryptocurrencyapp.coin_data.model.cryptosearch

import java.io.Serializable

data class Coin(
    val id: String,
    val large: String,
    val market_cap_rank: Int,
    val name: String,
    val symbol: String,
    val thumb: String
):Serializable