package com.marioioannou.newsapp.news_data.model

data class News(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)