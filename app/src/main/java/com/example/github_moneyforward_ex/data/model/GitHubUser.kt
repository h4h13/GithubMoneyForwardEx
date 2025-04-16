package com.example.github_moneyforward_ex.data.model

import com.google.gson.annotations.SerializedName

data class GitHubUser(
    val login: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("name") val fullName: String?,
    val followers: Int,
    @SerializedName("following") val following: Int
)