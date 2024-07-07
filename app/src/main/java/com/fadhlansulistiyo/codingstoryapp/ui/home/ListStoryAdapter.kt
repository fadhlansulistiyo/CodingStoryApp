package com.fadhlansulistiyo.codingstoryapp.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fadhlansulistiyo.codingstoryapp.data.response.ListStoryItem
import com.fadhlansulistiyo.codingstoryapp.databinding.ItemStoryBinding
import com.fadhlansulistiyo.codingstoryapp.ui.detailstory.DetailStoriesActivity

class ListStoryAdapter : ListAdapter<ListStoryItem, ListStoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListStoryItem) {
            binding.tvItemName.text = item.name
            binding.tvItemDescription.text = item.description
            Glide.with(itemView.context)
                .load(item.photoUrl)
                .into(binding.ivItemPhoto)

            itemView.setOnClickListener {
                Intent(itemView.context, DetailStoriesActivity::class.java).apply {
                    putExtra(DetailStoriesActivity.EXTRA_ID, item.id)
                    putExtra(DetailStoriesActivity.EXTRA_NAME, item.name)
                }.run {
                    itemView.context.startActivity(this)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ListStoryItem> =
            object : DiffUtil.ItemCallback<ListStoryItem>() {
                override fun areItemsTheSame(
                    oldItem: ListStoryItem,
                    newItem: ListStoryItem
                ): Boolean {
                    return oldItem.name == newItem.name
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