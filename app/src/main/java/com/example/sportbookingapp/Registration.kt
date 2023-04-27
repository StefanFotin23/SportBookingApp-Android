package com.example.sportbookingapp

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception
import java.util.logging.Logger

class Registration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        onResume()
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        var email: String
        var password: String
        var username: String
        var fullName: String
        var phoneNumber: String

        if (intent != null) {
            email = intent.getStringExtra("email").toString()
            password = intent.getStringExtra("password").toString()
            username = intent.getStringExtra("userName").toString()
            fullName = intent.getStringExtra("fullName").toString()
            phoneNumber = intent.getStringExtra("phoneNumber").toString()
        }
        val backButton = findViewById<TextView>(R.id.backButton)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val additionalMessage = findViewById<TextView>(R.id.additionalMessageTextView)
        val editTextEmail = findViewById<TextInputEditText>(R.id.loginEmailTextInput)
        val editTextPassword = findViewById<TextInputEditText>(R.id.loginPasswordTextInput)
        val editTextUsername = findViewById<TextInputEditText>(R.id.userNameTextInput)
        val editTextFullName = findViewById<TextInputEditText>(R.id.fullNameTextInput)
        val editTextPhoneNumber = findViewById<TextInputEditText>(R.id.phoneNumberTextInput)

        backButton.setOnClickListener {
            val loginActivity = Intent(this@Registration, Login::class.java)
            email = editTextEmail.text.toString()
            password = editTextPassword.text.toString()
            username = editTextUsername.text.toString()
            fullName = editTextFullName.text.toString()
            phoneNumber = editTextPhoneNumber.text.toString()
            loginActivity.putExtra("email", email)
            loginActivity.putExtra("password", password)
            loginActivity.putExtra("username", username)
            loginActivity.putExtra("fullName", fullName)
            loginActivity.putExtra("phoneNumber", phoneNumber)
            this@Registration.startActivity(loginActivity)
        }

        registerButton.setOnClickListener {
            email = editTextEmail.text.toString()
            password = editTextPassword.text.toString()
            username = editTextUsername.text.toString()
            fullName = editTextFullName.text.toString()
            phoneNumber = editTextPhoneNumber.text.toString()

            additionalMessage.isVisible = false
            var valid = true

            if (valid && !usernameValid(username)) {
                additionalMessage.text = "Username already used."
                additionalMessage.isVisible = true
                valid = false
            }

            if (valid && !emailFormatValid(email)) {
                additionalMessage.text = "Email has invalid format. (ex.: nicusor@yahoo.com)"
                additionalMessage.isVisible = true
                valid = false
            }

            if (valid && !passwordFormatValid(password)) {
                additionalMessage.text = "Password must contain at least 6 characters."
                additionalMessage.isVisible = true
                valid = false
            }

            if (valid && !phoneNumberFormatValid(phoneNumber)) {
                additionalMessage.text = "Phone number must be 10 digits long."
                additionalMessage.isVisible = true
                valid = false
            }

            if (valid && !fullNameFormatValid(fullName)) {
                additionalMessage.text = "Full Name can't be empty."
                additionalMessage.isVisible = true
                valid = false
            }

            if (valid) {
                try {
                    firebaseSignUp(email, password)
                } catch (e: Exception) {
                    val logger = Logger.getLogger(this.javaClass.name)
                    logger.warning(e.message)
                }
            }
        }
    }

    private fun emailFormatValid(email: String): Boolean {
        if (email == "Email") {
            return false
        }
        return true
    }

    private fun passwordFormatValid(password: String): Boolean {
        if (password == "Password") {
            return false
        }
        return password.length >= 6
    }

    private fun phoneNumberFormatValid(phoneNumber: String): Boolean {
        if (phoneNumber == "Phone Number") {
            return false
        }
        return phoneNumber.length == 10
    }

    private fun fullNameFormatValid(name: String): Boolean {
        return name != "Full Name"
    }

    private fun usernameValid(username: String): Boolean {
        if (username == "Username") {
            return false
        }
        // itereaza prin usernames si returneaza rezultat
        

        return true
    }

    private fun firebaseSignUp(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        var progressBar = findViewById<ProgressBar>(R.id.registrationProgressBar)
        progressBar.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    val loginActivity = Intent(this@Registration, Login::class.java)
                    loginActivity.putExtra("email", email)
                    this@Registration.startActivity(loginActivity)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}