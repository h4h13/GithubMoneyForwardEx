package com.example.github_moneyforward_ex.data.repository

import com.example.github_moneyforward_ex.data.api.GitHubApiService
import com.example.github_moneyforward_ex.data.api.RetrofitClient
import com.example.github_moneyforward_ex.data.model.GitHubUser
import com.example.github_moneyforward_ex.data.model.GitHubUserBrief
import com.example.github_moneyforward_ex.data.model.Repo
import javax.inject.Inject

class GitHubRepository @Inject constructor(
    private val apiService: GitHubApiService
) {

    suspend fun searchUsers(query: String): List<GitHubUserBrief> {
        return apiService.searchUsers(query).items
    }

    suspend fun getUserDetails(username: String): GitHubUser {
        return apiService.getUserDetails(username)
    }

    suspend fun getUserRepos(username: String): List<Repo> {
        return apiService.getUserRepos(username)//.filter { it.fork }
    }
}