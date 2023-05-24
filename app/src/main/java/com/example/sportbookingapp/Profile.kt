package com.example.sportbookingapp

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class Profile : Fragment() {
    private var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val logoutTextView = view.findViewById<TextView>(R.id.logout_textView)
        logoutTextView.setOnClickListener {
            FirebaseAuth.getInstance().signOut() // clear the user session
            val loginIntent = Intent(context, Login::class.java)
            startActivity(loginIntent) // redirect the user to the login page
            // finish() // close the current activity
        }

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                Log.d("fetch", "FETCH_RESULT : " + result.documents + "\n")
                for (document in result.documents) {
                    val email = document.getString("email")
                    val firstName = document.getString("firstName")
                    val lastName = document.getString("lastName")
                    val phoneNumber = document.getString("phoneNumber")

                    // Set the retrieved profile data to the corresponding EditText fields
                    view.findViewById<EditText>(R.id.emailEditText).setText(email)
                    view.findViewById<EditText>(R.id.firstNameEditText).setText(firstName)
                    view.findViewById<EditText>(R.id.lastNameEditText).setText(lastName)
                    view.findViewById<EditText>(R.id.phoneNumberEditText).setText(phoneNumber)
                }
            }
            .addOnFailureListener { exception ->
                // Error occurred while retrieving profile data
                Log.e(TAG, "Error retrieving profile data: ${exception.message}")
            }

        // Get a reference to the Save button
        val saveButton = view.findViewById<Button>(R.id.saveButton)

        // Set a click listener on the Save button
        saveButton.setOnClickListener {
            // Get the updated values from the TextView fields
            val email = view.findViewById<TextView>(R.id.emailEditText).text.toString()
            val firstName = view.findViewById<TextView>(R.id.firstNameEditText).text.toString()
            val lastName = view.findViewById<TextView>(R.id.lastNameEditText).text.toString()
            val phoneNumber = view.findViewById<TextView>(R.id.phoneNumberEditText).text.toString()

            // Create a new user document with the profile data
            val user = HashMap<String, Any>()
            user["email"] = email
            user["firstName"] = firstName
            user["lastName"] = lastName
            user["phoneNumber"] = phoneNumber

            // Add the user document to the "users" collection and let Firebase generate a unique ID
            db.collection("users")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    // User document added successfully
                    val userId = documentReference.id
                    Log.d(TAG, "User document added with ID: $userId")

                    // Perform any additional operations here
                }
                .addOnFailureListener { exception ->
                    // Error occurred while adding user document
                    Log.e(TAG, "Error adding user document: ${exception.message}")
                }
        }

        return view
    }
}
