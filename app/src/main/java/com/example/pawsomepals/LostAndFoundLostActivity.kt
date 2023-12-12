package com.example.pawsomepals

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LostAndFoundLostActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var lostPetsAdapter: LostAndFoundLostAdapter
    private val lostPetsList: MutableList<LostPet> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lost_and_found_lost)

        val reportLostPetButton = findViewById<Button>(R.id.buttonLostForm)

        // Initialize the RecyclerView and adapter
        val recyclerView: RecyclerView = findViewById(R.id.rv_lost)
        recyclerView.layoutManager = LinearLayoutManager(this)
        lostPetsAdapter = LostAndFoundLostAdapter(lostPetsList, this)
        recyclerView.adapter = lostPetsAdapter

        // Initialize the Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().reference.child("lost_and_found_lost")

        // Set up a ValueEventListener to retrieve data from Firebase
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lostPetsList.clear()
                for (postSnapshot in snapshot.children) {
                    val lostPet = postSnapshot.getValue(LostPet::class.java)
                    if (lostPet != null) {
                        // Retrieve the image URL from the database (it can be null)
                        val imageURL: String? = postSnapshot.child("imageURL").getValue(String::class.java)

                        Log.d("ImageURL", "Image URL: $imageURL")

                        // Use the Elvis operator (?:) to provide a default value if imageUrl is null
                        lostPet.imageURL = imageURL ?: "default_image_url"

                        Log.d("ImageURL", "Image URL: ${lostPet.imageURL}")

                        lostPetsList.add(lostPet)
                    }
                }
                lostPetsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })

        lostPetsAdapter.setOnItemClickListener(object : LostAndFoundLostAdapter.OnItemClickListener {
            override fun onItemClick(lostPet: LostPet) {
                // Handle item click here, start the LostPetDetails activity
                val intent = Intent(this@LostAndFoundLostActivity, LostAndFoundLostPetDetailsActivity::class.java)
                intent.putExtra("lostPet", lostPet) // Pass the selected LostPet object
                startActivity(intent)
            }
        })

        reportLostPetButton.setOnClickListener {
            val intent = Intent(this, LostAndFoundLostFormActivity::class.java)
            startActivity(intent)
        }
    }
}