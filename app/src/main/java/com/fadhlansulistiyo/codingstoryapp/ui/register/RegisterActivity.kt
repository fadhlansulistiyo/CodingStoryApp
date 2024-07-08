package com.fadhlansulistiyo.codingstoryapp.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fadhlansulistiyo.codingstoryapp.R
import com.fadhlansulistiyo.codingstoryapp.data.ResultState
import com.fadhlansulistiyo.codingstoryapp.databinding.ActivityRegisterBinding
import com.fadhlansulistiyo.codingstoryapp.ui.ViewModelFactory
import com.fadhlansulistiyo.codingstoryapp.ui.login.LoginActivity
import com.fadhlansulistiyo.codingstoryapp.ui.util.EmailValidator
import com.fadhlansulistiyo.codingstoryapp.ui.util.PasswordValidator
import com.fadhlansulistiyo.codingstoryapp.ui.util.isValidEmail
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupAction()
        playAnimation()
        setupValidators()
    }

    private fun setupAction() {
        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if (name.isEmpty()) {
                binding.edRegisterName.error = getString(R.string.cannot_empty)
            }

            if (!isValidEmail(email)) {
                binding.edRegisterEmail.error = getString(R.string.invalid_email)
                return@setOnClickListener
            }

            if (password.length < 8) {
                binding.edRegisterPassword.error
                return@setOnClickListener
            }

            viewModel.register(name, email, password).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Success -> {
                            showLoading(false)
                            showSnackbar(this, result.data.message)

                            // if success
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }

                        is ResultState.Error -> {
                            showLoading(false)
                            showToast(result.error)
                        }
                    }
                }
            }
        }

        binding.tvToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setupValidators() {
        binding.edRegisterEmail.setValidator(EmailValidator())
        binding.edRegisterPassword.setValidator(PasswordValidator())
    }

    private fun showSnackbar(activity: Activity, message: String) {
        Snackbar.make(
            activity.findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(100)
        val subtitle =
            ObjectAnimator.ofFloat(binding.tvSubtitle, View.ALPHA, 1f).setDuration(100)
        val layoutRegisterName =
            ObjectAnimator.ofFloat(binding.layoutRegisterName, View.ALPHA, 1f).setDuration(100)
        val layoutRegisterEmail =
            ObjectAnimator.ofFloat(binding.layoutRegisterEmail, View.ALPHA, 1f).setDuration(100)
        val layoutRegisterPassword =
            ObjectAnimator.ofFloat(binding.layoutRegisterPassword, View.ALPHA, 1f).setDuration(100)
        val btnRegister =
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(100)
        val toLogin =
            ObjectAnimator.ofFloat(binding.toLogin, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                subtitle,
                layoutRegisterName,
                layoutRegisterEmail,
                layoutRegisterPassword,
                btnRegister,
                toLogin
            )
            startDelay = 100
        }.start()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}