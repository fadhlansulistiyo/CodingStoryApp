package com.fadhlansulistiyo.codingstoryapp.ui.util

interface Validator {
    fun validate(text: String): Boolean
    fun getErrorMessage(): String
}

class PasswordValidator : Validator {
    override fun validate(text: String): Boolean {
        return text.length >= 8
    }

    override fun getErrorMessage(): String {
        return "Password must not be less than 8 characters"
    }
}

class EmailValidator : Validator {
    override fun validate(text: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()
    }

    override fun getErrorMessage(): String {
        return "Invalid email format"
    }
}
