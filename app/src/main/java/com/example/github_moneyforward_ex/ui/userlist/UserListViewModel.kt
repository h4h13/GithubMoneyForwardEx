package com.example.github_moneyforward_ex.ui.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github_moneyforward_ex.data.model.GitHubUserBrief
import com.example.github_moneyforward_ex.data.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UserListUiState {
    object Loading : UserListUiState()
    data class Success(val users: List<GitHubUserBrief>) : UserListUiState()
    data class Error(val errorMessage: String) : UserListUiState()
    object Idle : UserListUiState()
}

@OptIn(FlowPreview::class)
@HiltViewModel
class UserListViewModel
@Inject constructor(private val repository: GitHubRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<UserListUiState> = MutableStateFlow(UserListUiState.Idle)
    val uiState: StateFlow<UserListUiState> = _uiState //Corrected to uiState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val DEBOUNCE_TIMEOUT = 500L // 500 milliseconds debounce

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(DEBOUNCE_TIMEOUT)
                .collectLatest { query ->
                    if (query.isBlank() || query.length < 2) {
                        _uiState.value = UserListUiState.Idle
                    } else {
                        searchUsersFlow(query)
                            .onStart {
                                _uiState.value = UserListUiState.Loading
                            }
                            .catch { e ->
                                _uiState.value = UserListUiState.Error(e.message ?: "Unknown Error")
                            }
                            .collect { users ->
                                _uiState.value = UserListUiState.Success(users)
                            }
                    }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {

        _searchQuery.value = query
    }

    private fun searchUsersFlow(query: String): Flow<List<GitHubUserBrief>> = flow {
        val users = repository.searchUsers(query)
        emit(users)
    }
}

