package com.example.github_moneyforward_ex.ui.userdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.github_moneyforward_ex.databinding.FragmentUserDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class UserDetailsFragment : Fragment() {

    private var _binding: FragmentUserDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: UserDetailsViewModel by viewModels()
    private val args: UserDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadUserDetails(args.username)

        binding.recyclerRepos.layoutManager = LinearLayoutManager(requireContext())
        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.textUsername.text = user.login
            binding.textFullName.text = user.fullName ?: "No name provided"
            binding.textFollowers.text = "Followers: ${user.followers}"
            binding.textFollowing.text = "Following: ${user.following}"
            Glide.with(this).load(user.avatarUrl).into(binding.imageAvatar)
        }
        viewModel.repos.observe(viewLifecycleOwner) { repos ->
            binding.recyclerRepos.adapter = RepoAdapter(repos)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}