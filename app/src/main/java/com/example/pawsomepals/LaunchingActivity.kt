package com.example.pawsomepals

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView

class LaunchingActivity : AppCompatActivity() {

    private lateinit var textViewLoading: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launching)

        supportActionBar?.hide()

        val packageName = applicationContext.packageName
        val preferencesName = "$packageName.user_preferences"
        val sharedPreferences = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("userEmail","").toString()

        textViewLoading = findViewById(R.id.tvLoading)




        Handler(Looper.getMainLooper()).postDelayed({
            textViewLoading.text = "Validating login..."

            Handler(Looper.getMainLooper()).postDelayed({
                if(userEmail.isNullOrEmpty() && userEmail.isNullOrBlank()){
                    textViewLoading.text = "Logging in as guest..."
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish() // Close the LaunchingActivity
                    }, 2000)
                }else{
                    textViewLoading.text = "Logging in as ${userEmail}"
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish() // Close the LaunchingActivity
                    }, 2000)
                }
            }, 2000)
        }, 2000) // 5000 milliseconds (5 seconds)
    }
}