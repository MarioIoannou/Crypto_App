package com.marioioannou.newsapp.news_data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class Article(
    @PrimaryKey(autoGenerate = true) //Autogenerate id number
    var id: Int? = null, //It is null because not all articles have ids
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
):Serializable