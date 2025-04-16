package com.example.github_moneyforward_ex.ui.userdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github_moneyforward_ex.data.model.GitHubUser
import com.example.github_moneyforward_ex.data.model.Repo
import com.example.github_moneyforward_ex.data.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UserDetailsUiState {
    object Idle : UserDetailsUiState()
    object Loading : UserDetailsUiState()
    data class Success(val user: GitHubUser, val repos: List<Repo>) : UserDetailsUiState()
    data class Error(val errorMessage: String) : UserDetailsUiState()
}

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val repository: GitHubRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserDetailsUiState>(UserDetailsUiState.Idle)
    val uiState: StateFlow<UserDetailsUiState> = _uiState

    fun loadUserDetails(username: String) {
        viewModelScope.launch {
            loadUserReposFlow(username)
                .onStart {
                    _uiState.value = UserDetailsUiState.Loading
                }
                .catch {
                    _uiState.value = UserDetailsUiState.Error(it.message ?: "Unknown Error")
                }
                .collect {
                    _uiState.value = it
                }
        }
    }

    private fun loadUserReposFlow(username: String): Flow<UserDetailsUiState> = flow {
        val user = repository.getUserDetails(username)
        val repos = repository.getUserRepos(username)
        emit(UserDetailsUiState.Success(user, repos))
    }
}