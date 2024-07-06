package com.fadhlansulistiyo.codingstoryapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.fadhlansulistiyo.codingstoryapp.R
import com.fadhlansulistiyo.codingstoryapp.data.ResultState
import com.fadhlansulistiyo.codingstoryapp.databinding.ActivityHomeBinding
import com.fadhlansulistiyo.codingstoryapp.ui.ViewModelFactory
import com.fadhlansulistiyo.codingstoryapp.ui.addstory.AddStoryActivity
import com.fadhlansulistiyo.codingstoryapp.ui.main.MainActivity

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        observeStories()
        setupAction()
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
                    }

                    is ResultState.Success -> {
                        showLoading(false)
                        Log.d(this@HomeActivity.toString(), "${result.data}")

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


    private fun setupAction() {
        binding.apply {
            btnLogout.setOnClickListener {
                viewModel.logout()
                val intent = Intent(this@HomeActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
            Log.d("HomeActivity", "Add Story")
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
