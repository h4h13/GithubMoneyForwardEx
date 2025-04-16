package com.example.github_moneyforward_ex.data.model

import com.google.gson.annotations.SerializedName

data class GitHubUserBrief(
    val login: String,
    @SerializedName("avatar_url") val avatarUrl: String
)