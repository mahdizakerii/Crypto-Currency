package com.example.dunipool.apiManager.model


import com.google.gson.annotations.SerializedName

data class NewsData(
    @SerializedName("Data")
    val `data`: List<Data>,
    @SerializedName("HasWarning")
    val hasWarning: Boolean,
    @SerializedName("Message")
    val message: String,
    @SerializedName("Type")
    val type: Int
) {
    data class Data(
        @SerializedName("body")
        val body: String,
        @SerializedName("categories")
        val categories: String,
        @SerializedName("downvotes")
        val downvotes: String,
        @SerializedName("guid")
        val guid: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("imageurl")
        val imageurl: String,
        @SerializedName("lang")
        val lang: String,
        @SerializedName("published_on")
        val publishedOn: Int,
        @SerializedName("source")
        val source: String,
        @SerializedName("tags")
        val tags: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("upvotes")
        val upvotes: String,
        @SerializedName("url")
        val url: String
    )
}