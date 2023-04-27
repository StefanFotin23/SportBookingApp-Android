package com.example.sportbookingapp

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        onResume()
    }

    override fun onResume() {
        super.onResume()
        val passwordTextInputEditText =
            findViewById<TextInputEditText>(R.id.loginPasswordTextInput)
        passwordTextInputEditText.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        val sharedPreference = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
        findViewById<TextInputEditText>(R.id.loginEmailTextInput).setText(
            sharedPreference.getString("username", "")
        )

        val registerButton = findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            val registerActivity = Intent(this@Login, Registration::class.java)
            // get data from Registration intent, if we clicked "Back"
            if (intent != null) {
                val email = intent.getStringExtra("email")
                val password = intent.getStringExtra("password")
                val username = intent.getStringExtra("userName")
                val fullName = intent.getStringExtra("fullName")
                val phoneNumber = intent.getStringExtra("phoneNumber")

                // send data to the Registration Activity
                registerActivity.putExtra("email", email)
                registerActivity.putExtra("password", password)
                registerActivity.putExtra("username", username)
                registerActivity.putExtra("fullName", fullName)
                registerActivity.putExtra("phoneNumber", phoneNumber)
            }
            this@Login.startActivity(registerActivity)
        }

        val checkBox = findViewById<CheckBox>(R.id.passwordCheckBox)
        checkBox.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                passwordTextInputEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                passwordTextInputEditText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            val emailTextInputEditText = findViewById<TextInputEditText>(R.id.loginEmailTextInput)
            try {
                firebaseSignIn(
                    emailTextInputEditText.text.toString(),
                    passwordTextInputEditText.text.toString()
                )
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
            }
        }
    }

    private fun firebaseSignIn(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        val progressBar = findViewById<ProgressBar>(R.id.logInProgressBar)
        var validCredentials = true

        if (email == "" || email == "Email") {
            validCredentials = false
            findViewById<TextView>(R.id.additionalMessageTextView).visibility =
                View.VISIBLE
            findViewById<TextInputEditText>(R.id.loginEmailTextInput).setText("")
            findViewById<TextInputEditText>(R.id.loginPasswordTextInput).setText("")
        }

        if (password == "" || password == "Password") {
            validCredentials = false
            findViewById<TextView>(R.id.additionalMessageTextView).visibility =
                View.VISIBLE
            findViewById<TextInputEditText>(R.id.loginPasswordTextInput).setText("")
        }

        if (validCredentials) {
            progressBar.visibility = View.VISIBLE
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        val mainActivity = Intent(this@Login, MainActivity::class.java)
                        if (user != null) {
                            mainActivity.putExtra("uid", user.uid)
                            mainActivity.putExtra("email", email)
                        }
                        this@Login.startActivity(mainActivity)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        findViewById<TextView>(R.id.additionalMessageTextView).visibility =
                            View.VISIBLE
                        findViewById<TextInputEditText>(R.id.loginEmailTextInput).setText("")
                        findViewById<TextInputEditText>(R.id.loginPasswordTextInput).setText("")
                    }
                }
        }
    }
}