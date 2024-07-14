package com.fadhlansulistiyo.codingstoryapp.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fadhlansulistiyo.codingstoryapp.data.model.ListStoriesItem
import com.fadhlansulistiyo.codingstoryapp.databinding.ItemStoryBinding
import com.fadhlansulistiyo.codingstoryapp.ui.detailstory.DetailStoriesActivity
import com.fadhlansulistiyo.codingstoryapp.ui.util.DateFormatter
import java.util.TimeZone

class ListStoryAdapter : PagingDataAdapter<ListStoriesItem, ListStoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListStoriesItem) {
            binding.tvItemName.text = item.name
            binding.tvItemDescription.text = item.description
            binding.tvItemDate.text =
                DateFormatter.formatDate(item.createdAt.toString(), TimeZone.getDefault().id)

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
        if (story != null) {
            holder.bind(story)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ListStoriesItem> =
            object : DiffUtil.ItemCallback<ListStoriesItem>() {
                override fun areItemsTheSame(
                    oldItem: ListStoriesItem,
                    newItem: ListStoriesItem
                ): Boolean {
                    return oldItem == newItem
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: ListStoriesItem,
                    newItem: ListStoriesItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }
            }
    }
}