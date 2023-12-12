package com.example.pawsomepals

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.pawsomepals.dataclass.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()

        editTextName = findViewById(R.id.editTextRegisterName)
        editTextEmail = findViewById(R.id.editTextRegisterEmail)
        editTextPhone = findViewById(R.id.editTextRegisterPhone)
        editTextPassword = findViewById(R.id.editTextRegisterPassword)
        editTextConfirmPassword = findViewById(R.id.editTextRegisterConfirmPassword)

        var btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener{
            validateDetails()
        }
    }

    private fun validateDetails() {
        var nameText = editTextName.text.toString()
        var emailText = editTextEmail.text.toString()
        var phoneText = editTextPhone.text.toString()
        var passwordText = editTextPassword.text.toString()
        var confirmPasswordText = editTextConfirmPassword.text.toString()

        if(nameText.isNotBlank() &&
                emailText.isNotBlank() &&
                phoneText.isNotBlank() &&
                passwordText.isNotBlank() &&
                confirmPasswordText.isNotBlank()){
            if (passwordText == confirmPasswordText){
                databaseReference = FirebaseDatabase.getInstance().getReference("user")
                val user = User(nameText, emailText, phoneText, passwordText)
                databaseReference.push().setValue(user).addOnSuccessListener {

                    // Get shared preferences to store user data
                    val packageName = applicationContext.packageName
                    val preferencesName = "$packageName.user_preferences"
                    val sharedPreferences = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)

                    // Edit shared preferences to store user data
                    val  editor = sharedPreferences.edit()
                    editor.putString("userEmail",emailText)
                    editor.apply()
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to register the user", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Password confirmation is failed", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
        }


    }
}