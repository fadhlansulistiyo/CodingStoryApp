package com.fadhlansulistiyo.codingstoryapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fadhlansulistiyo.codingstoryapp.R
import com.fadhlansulistiyo.codingstoryapp.databinding.ActivityHomeBinding
import com.fadhlansulistiyo.codingstoryapp.ui.ViewModelFactory
import com.fadhlansulistiyo.codingstoryapp.ui.addstory.AddStoryActivity
import com.fadhlansulistiyo.codingstoryapp.ui.main.MainActivity
import com.fadhlansulistiyo.codingstoryapp.ui.maps.MapsStoryActivity
import com.google.android.material.color.MaterialColors

class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var storyAdapter: ListStoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set Up Toolbar
        val appBarLayout = binding.appBarLayout
        setSupportActionBar(binding.toolbar)
        binding.appBarLayout.setStatusBarForegroundColor(
            MaterialColors.getColor(appBarLayout, R.attr.colorSurface)
        )

        storyAdapter = ListStoryAdapter()
        binding.recyclerViewStory.layoutManager = LinearLayoutManager(this)

        setupScrollListener()
        setupScrollToTopFab()
        observeStories()
        observeNewData()
    }

    private fun observeStories() {
        viewModel.stories.observe(this) {
            storyAdapter.submitData(lifecycle, it)
        }

        binding.recyclerViewStory.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )

        storyAdapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    showLoading(true)

                }

                is LoadState.NotLoading -> {
                    showLoading(false)
                }

                is LoadState.Error -> {
                    showLoading(false)
                    val errorState = loadState.refresh as LoadState.Error
                    showToast(errorState.error.message.toString())
                }
            }
        }
    }

    private fun observeNewData() {
        viewModel.hasNewData.observe(this) { hasNewData ->
            if (hasNewData) {
                binding.fabScrollToTop.show()
            }
        }
    }

    private fun logout() {
        viewModel.logout()
        val intent = Intent(this@HomeActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupScrollListener() {
        binding.recyclerViewStory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    binding.fabScrollToTop.show()
                } else {
                    binding.fabScrollToTop.hide()
                }
            }
        })
    }

    private fun setupScrollToTopFab() {
        binding.fabScrollToTop.setOnClickListener {
            binding.recyclerViewStory.smoothScrollToPosition(0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logout()
                true
            }

            R.id.action_add_story -> {
                val intent = Intent(this, AddStoryActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.action_maps -> {
                val intent = Intent(this, MapsStoryActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.mainProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
