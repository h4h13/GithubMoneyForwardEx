package com.example.github_moneyforward_ex.data.model

import com.google.gson.annotations.SerializedName

data class Repo(
    val name: String,
    @SerializedName("language") val language: String?,
    @SerializedName("stargazers_count") val starCount: Int,
    val description: String?,
    val fork: Boolean,
    @SerializedName("html_url") val htmlUrl: String
)