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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fadhlansulistiyo.codingstoryapp.R
import com.fadhlansulistiyo.codingstoryapp.data.ResultState
import com.fadhlansulistiyo.codingstoryapp.databinding.ActivityHomeBinding
import com.fadhlansulistiyo.codingstoryapp.ui.ViewModelFactory
import com.fadhlansulistiyo.codingstoryapp.ui.addstory.AddStoryActivity
import com.fadhlansulistiyo.codingstoryapp.ui.main.MainActivity
import com.google.android.material.color.MaterialColors

class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }

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

        observeStories()
        addStory()
    }

    private fun observeStories() {
        val storyAdapter = ListStoryAdapter()
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewStory.layoutManager = layoutManager

        viewModel.getStories().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                        showFab(false)
                    }

                    is ResultState.Success -> {
                        showLoading(false)
                        showFab(true)

                        val storyData = result.data.listStory
                        storyAdapter.submitList(storyData)
                        binding.recyclerViewStory.adapter = storyAdapter
                    }

                    is ResultState.Error -> {
                        showLoading(false)
                        showToast(result.error)
                    }
                }
            }
        }
    }

    private fun logout() {
        viewModel.logout()
        val intent = Intent(this@HomeActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showFab(isVisible: Boolean) {
        binding.fabAddStory.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun addStory() {
        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.mainProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        observeStories()
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

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
