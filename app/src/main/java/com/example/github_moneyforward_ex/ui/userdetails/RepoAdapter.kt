package com.example.github_moneyforward_ex.ui.userdetails

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.github_moneyforward_ex.data.model.Repo
import com.example.github_moneyforward_ex.databinding.ItemRepoBinding

class RepoAdapter(private val repos: List<Repo>) :
    RecyclerView.Adapter<RepoAdapter.RepoViewHolder>() {

    inner class RepoViewHolder(private val binding: ItemRepoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(repo: Repo) {
            binding.textRepoName.text = repo.name
            binding.textLanguage.text = repo.language ?: "Unknown"
            binding.textStars.text = "★ ${repo.starCount}"
            binding.textDescription.text = repo.description ?: "No description"

            binding.root.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(repo.htmlUrl))
                it.context.startActivity(browserIntent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding = ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoViewHolder(binding)
    }

    override fun getItemCount() = repos.size

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(repos[position])
    }
}
