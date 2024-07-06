package com.fadhlansulistiyo.codingstoryapp.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fadhlansulistiyo.codingstoryapp.R
import com.fadhlansulistiyo.codingstoryapp.databinding.ActivityMainBinding
import com.fadhlansulistiyo.codingstoryapp.ui.ViewModelFactory
import com.fadhlansulistiyo.codingstoryapp.ui.home.HomeActivity
import com.fadhlansulistiyo.codingstoryapp.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.getSession().observe(this) { user ->
            val intent = Intent(
                this,
                if (user.isLogin) HomeActivity::class.java else LoginActivity::class.java
            )
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}