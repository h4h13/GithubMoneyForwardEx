package com.example.github_moneyforward_ex.data.api

import com.example.github_moneyforward_ex.data.model.GitHubUser
import com.example.github_moneyforward_ex.data.model.Repo
import com.example.github_moneyforward_ex.data.model.UserSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
    ): UserSearchResponse

    @GET("users/{username}")
    suspend fun getUserDetails(
        @Path("username") username: String,
    ): GitHubUser

    @GET("users/{username}/repos")
    suspend fun getUserRepos(
        @Path("username") username: String
    ): List<Repo>
}