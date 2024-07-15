package com.fadhlansulistiyo.codingstoryapp.ui.detailstory

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.fadhlansulistiyo.codingstoryapp.R
import com.fadhlansulistiyo.codingstoryapp.data.ResultState
import com.fadhlansulistiyo.codingstoryapp.data.response.Story
import com.fadhlansulistiyo.codingstoryapp.databinding.ActivityDetailStoriesBinding
import com.fadhlansulistiyo.codingstoryapp.ui.ViewModelFactory
import com.fadhlansulistiyo.codingstoryapp.ui.util.DateFormatter
import java.util.TimeZone

class DetailStoriesActivity : AppCompatActivity() {

    private var _binding: ActivityDetailStoriesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailStoriesViewModel> {
        ViewModelFactory.getInstance(this)
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAME = "extra_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityDetailStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_ID) ?: return
        val name = intent.getStringExtra(EXTRA_NAME) ?: getString(R.string.name)

        setupToolbar(name)
        observeDetailStories(id)
    }

    private fun observeDetailStories(id: String) {
        viewModel.getDetailStories(id).observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    showLoading(true)
                }

                is ResultState.Success -> {
                    showLoading(false)
                    result.data.story?.let { setDetailStory(it) }
                }

                is ResultState.Error -> {
                    showLoading(false)
                    showToast(result.error)
                }
            }

        }
    }

    private fun setDetailStory(story: Story) {
        binding.apply {
            tvDetailName.text = story.name
            tvDetailDescription.text = story.description
            tvDetailDate.text =
                DateFormatter.formatDate(story.createdAt.toString(), TimeZone.getDefault().id)

            Glide.with(this@DetailStoriesActivity)
                .load(story.photoUrl)
                .into(ivDetailPhoto)
        }
    }

    private fun setupToolbar(name: String) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = name
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}