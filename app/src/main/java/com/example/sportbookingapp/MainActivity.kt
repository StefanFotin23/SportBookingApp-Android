package com.example.sportbookingapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sportbookingapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onResume()
    }

    override fun onResume() {
        super.onResume()
        replaceFragment(Home())

        val uid = intent.getStringExtra("uid")
        val sharedPreference =  getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString("username", intent.getStringExtra("email"))
        editor.commit()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> replaceFragment(Home())
                R.id.reservationStatus -> replaceFragment(ReservationStatus())
                R.id.profile -> replaceFragment(Profile())
                else -> {
                    // do nothing
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}