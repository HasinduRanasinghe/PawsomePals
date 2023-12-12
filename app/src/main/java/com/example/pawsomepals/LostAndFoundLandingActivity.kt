package com.example.pawsomepals

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class LostAndFoundLandingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lost_and_found_landing)

        // Find the MaterialCardView elements for lost and found cards
        val lostCard = findViewById<MaterialCardView>(R.id.cv_lnf_lostCard)
        val foundCard = findViewById<MaterialCardView>(R.id.cv_lnf_foundCard)

        // Set click listeners for the cards
        lostCard.setOnClickListener {
            // Start the LostAndFoundLostActivity when the Lost card is clicked
            val intent = Intent(this, LostAndFoundLostActivity::class.java)
            startActivity(intent)
        }

        foundCard.setOnClickListener {
            // Start the LostAndFoundFoundActivity when the Found card is clicked
            val intent = Intent(this, LostAndFoundFoundActivity::class.java)
            startActivity(intent)
        }
    }
}
