package com.marioioannou.cryptopal.coin_data.model.coin_news

import java.io.Serializable

data class New(
    val coins: List<Any>?,
    val content: Boolean?,
    val description: String?,
    val feedDate: Long?,
    val icon: String?,
    val id: String?,
    val imgURL: String?,
    val link: String?,
    val reactionsCount: ReactionsCount?,
    val relatedCoins: List<Any>?,
    val shareURL: String?,
    val source: String?,
    val sourceLink: String?,
    val title: String?
): Serializable