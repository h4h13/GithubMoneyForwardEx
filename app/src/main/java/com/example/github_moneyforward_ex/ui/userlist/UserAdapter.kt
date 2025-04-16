package com.example.github_moneyforward_ex.ui.userlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.github_moneyforward_ex.data.model.GitHubUserBrief
import com.example.github_moneyforward_ex.databinding.ItemUserBinding

class UserAdapter(
    private val users: MutableList<GitHubUserBrief>,
    private val onClick: (GitHubUserBrief) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: GitHubUserBrief) {
            binding.textUsername.text = user.login
            Glide.with(binding.root.context).load(user.avatarUrl).into(binding.imageAvatar)
            binding.root.setOnClickListener {
                onClick(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    fun updateUsers(briefs: List<GitHubUserBrief>) {
        users.clear()
        users.addAll(briefs)
        notifyDataSetChanged()
    }
}