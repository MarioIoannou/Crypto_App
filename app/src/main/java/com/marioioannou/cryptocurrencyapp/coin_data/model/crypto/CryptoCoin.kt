package com.marioioannou.cryptocurrencyapp.coin_data.model.crypto

import java.io.Serializable

data class CryptoCoin(
    val ath: Double?,
    val ath_change_percentage: Double?,
    val ath_date: String?,
    val atl: Double?,
    val atl_change_percentage: Double?,
    val atl_date: String?,
    val circulating_supply: Double?,
    val current_price: Double?,
    val fully_diluted_valuation: Any?,
    val high_24h: Double?,
    val id: String?,
    val image: String?,
    val last_updated: String?,
    val low_24h: Double?,
    val market_cap: Long?,
    val market_cap_change_24h: Double?,
    val market_cap_change_percentage_24h: Double?,
    val market_cap_rank: Int?,
    val max_supply: Any?,
    val name: String?,
    val price_change_24h: Double?,
    val price_change_percentage_24h: Double?,
    val roi: Roi?,
    val symbol: String?,
    val total_supply: Any?,
    val total_volume: Double?
): Serializable