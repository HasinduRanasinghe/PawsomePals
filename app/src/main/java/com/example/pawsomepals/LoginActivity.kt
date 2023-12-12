package com.example.pawsomepals

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var email:EditText
    private lateinit var password:EditText

    private var emailFound = false
    private lateinit var userID:String

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        email = findViewById(R.id.editTextRegisterName)
        password = findViewById(R.id.editTextLoginPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener{
            validateUser()
        }
    }

    private fun validateUser() {
        val emailText = email.text.toString()
        val passwordText = password.text.toString()

        databaseReference = FirebaseDatabase.getInstance().getReference("user")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (userSnapshot in dataSnapshot.children) {
                    val email = userSnapshot.child("email").getValue(String::class.java)
                    if (email == emailText) {
                        // Email found in the database
                        emailFound = true
                        userID = userSnapshot.key.toString()
                        break
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error if the query fails
            }
        })

        if(emailFound){
            databaseReference = FirebaseDatabase.getInstance().getReference("user")
            databaseReference.child(userID).get().addOnSuccessListener {
                if(it.exists()){
                    var emailDB = it.child("email").value.toString()
                    var passwordDB = it.child("password").value.toString()
                    var nameDB = it.child("name").value.toString()
                    runOnUiThread {
                        if(passwordText == passwordDB){

                            Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show()

                            // Get shared preferences to store user data
                            val packageName = applicationContext.packageName
                            val preferencesName = "$packageName.user_preferences"
                            val sharedPreferences = getSharedPreferences(preferencesName, MODE_PRIVATE)

                            // Edit shared preferences to store user data
                            val  editor = sharedPreferences.edit()
                            editor.putString("userEmail",emailDB)
                            editor.putString("userID",userID)
                            editor.putString("userName",nameDB)
                            editor.apply()

                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this, "Username or password is incorrect", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {

                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to validate data", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "Username or password is incorrect", Toast.LENGTH_SHORT).show()
        }
    }
}