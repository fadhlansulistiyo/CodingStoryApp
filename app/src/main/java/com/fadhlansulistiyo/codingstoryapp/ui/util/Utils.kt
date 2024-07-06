package com.fadhlansulistiyo.codingstoryapp.ui.util

fun isValidEmail(email: CharSequence): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}