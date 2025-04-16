package com.example.github_moneyforward_ex.ui.userdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github_moneyforward_ex.data.model.GitHubUser
import com.example.github_moneyforward_ex.data.model.Repo
import com.example.github_moneyforward_ex.data.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val repository: GitHubRepository
) : ViewModel() {

    private val _user = MutableLiveData<GitHubUser>()
    val user: LiveData<GitHubUser> = _user

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _repos = MutableLiveData<List<Repo>>()
    val repos: LiveData<List<Repo>> = _repos

    fun loadUserDetails(username: String) {
        viewModelScope.launch {
            try {
                _user.value = repository.getUserDetails(username)
                _repos.value = repository.getUserRepos(username)
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load data: ${e.localizedMessage}"
            }
        }
    }
}