package com.example.github_moneyforward_ex.ui.userlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github_moneyforward_ex.data.model.GitHubUserBrief
import com.example.github_moneyforward_ex.databinding.FragmentUsersListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class UserListFragment : Fragment() {

    private val viewModel: UserListViewModel by viewModels()

    private var _binding: FragmentUsersListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUsersListBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(requireContext())
        val userAdapter = UserAdapter(emptyList<GitHubUserBrief>().toMutableList()) { user ->
            val action = UserListFragmentDirections
                .actionUsersFragmentToUserDetailsFragment(user.login)
            findNavController().navigate(action)
        }
        binding.recyclerViewUsers.adapter = userAdapter
        // Collect the flow in a coroutine that repeats on the STARTED lifecycle state
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState -> // Changed viewModel.users to viewModel.uiState
                    when (uiState) {
                        is UserListUiState.Idle -> {
                            // Update UI for idle state (e.g., show a default view)
                            binding.progressBar.visibility = View.GONE
                        }

                        is UserListUiState.Loading -> {
                            // Update UI for loading state (e.g., show progress bar)
                            binding.progressBar.visibility = View.VISIBLE
                            binding.errorMessage.visibility = View.GONE
                        }

                        is UserListUiState.Success -> {
                            // Update UI for success state (e.g., show user list)
                            binding.progressBar.visibility = View.GONE
                            binding.errorMessage.visibility = View.GONE
                            //update the list
                            userAdapter.updateUsers(uiState.users)
                        }

                        is UserListUiState.Error -> {
                            // Update UI for error state (e.g., show error message)
                            binding.progressBar.visibility = View.GONE
                            binding.errorMessage.visibility = View.VISIBLE
                            binding.errorMessage.text = uiState.errorMessage
                        }
                    }
                }
            }
        }
        binding.editSearch.doOnTextChanged { text, _, _, _ ->
            viewModel.onSearchQueryChanged(text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}