package com.fadhlansulistiyo.codingstoryapp.ui.detailstory

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.fadhlansulistiyo.codingstoryapp.R
import com.fadhlansulistiyo.codingstoryapp.data.ResultState
import com.fadhlansulistiyo.codingstoryapp.data.response.Story
import com.fadhlansulistiyo.codingstoryapp.databinding.ActivityDetailStoriesBinding
import com.fadhlansulistiyo.codingstoryapp.ui.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getStringExtra(EXTRA_ID)
        val name = intent.getStringExtra(EXTRA_NAME)

        // SetUp Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = name
        }

        observeDetailStories(id.toString())
    }

    private fun observeDetailStories(id: String) {
        viewModel.getDetailStories(id).observe(this) { result ->
            if (result != null) {
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

    }

    private fun setDetailStory(story: Story) {
        binding.apply {
            tvDetailName.text = story.name
            tvDetailDescription.text = story.description
            tvDetailDate.text = DateFormatter.formatDate(story.createdAt.toString(), TimeZone.getDefault().id)

            Glide.with(this@DetailStoriesActivity)
                .load(story.photoUrl)
                .into(ivDetailPhoto)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showSnackbar(activity: Activity, message: String) {
        Snackbar.make(
            activity.findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}