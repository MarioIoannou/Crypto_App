package com.marioioannou.cryptopal.coin_data.model.trending_coins

data class TrendingCoins(
    val coins: MutableList<Coin>,
    val exchanges: List<Any>
)