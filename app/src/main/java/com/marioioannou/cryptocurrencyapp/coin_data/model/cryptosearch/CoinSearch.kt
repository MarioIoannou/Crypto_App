package com.marioioannou.cryptocurrencyapp.coin_data.model.cryptosearch

data class CoinSearch(
    val categories: List<Category>,
    val coins: List<Coin>,
    val exchanges: List<Exchange>,
    val icos: List<Any>,
    val nfts: List<Nft>
)