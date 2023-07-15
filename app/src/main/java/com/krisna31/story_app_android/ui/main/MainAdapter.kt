package com.krisna31.story_app_android.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.krisna31.story_app_android.data.api.response.ListStoryItem
import com.krisna31.story_app_android.databinding.ItemsStoryBinding
import com.krisna31.story_app_android.ui.detail_story.DetailStoryActivity

class MainAdapter : ListAdapter<ListStoryItem, MainAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemsStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class MyViewHolder(val binding: ItemsStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(storyItem: ListStoryItem) {
            Glide.with(itemView.context)
                .load(storyItem.photoUrl)
                .into(binding.ivStory)
            binding.tvOwner.text = storyItem.name
            binding.tvDeskripsi.text = addEllipsisAfterWords(storyItem.description)
            binding.root.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivStory, "photo"),
                        Pair(binding.tvOwner, "name"),
                        Pair(binding.tvDeskripsi, "desc"),
                    )
                intent.putExtra(DetailStoryActivity.EXTRA_STORY, storyItem.id)
                itemView.context.startActivity(
                    intent,
                    optionsCompat.toBundle()
                )
            }
        }

        private fun addEllipsisAfterWords(text: String, maxWords: Int = 10): String {
            val words = text.split(" ")
            return if (words.size > maxWords) {
                val slicedWords = words.subList(0, maxWords)
                val shortenedText = slicedWords.joinToString(" ")
                "$shortenedText..."
            } else {
                text
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
