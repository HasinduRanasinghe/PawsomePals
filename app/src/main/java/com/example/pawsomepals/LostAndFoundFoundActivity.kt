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

class LostAndFoundFoundActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var foundPetsAdapter: LostAndFoundFoundAdapter
    private val foundPetsList: MutableList<FoundPet> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lost_and_found_found)

        val reportFoundPetButton = findViewById<Button>(R.id.buttonFoundForm)

        // Initialize the RecyclerView and adapter
        val recyclerView: RecyclerView = findViewById(R.id.rv_found)
        recyclerView.layoutManager = LinearLayoutManager(this)
        foundPetsAdapter = LostAndFoundFoundAdapter(foundPetsList, this)
        recyclerView.adapter = foundPetsAdapter

        // Initialize the Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().reference.child("lost_and_found_found")

        // Set up a ValueEventListener to retrieve data from Firebase
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                foundPetsList.clear()
                for (postSnapshot in snapshot.children) {
                    val foundPet = postSnapshot.getValue(FoundPet::class.java)
                    if (foundPet != null) {
                        // Retrieve the image URL from the database (it can be null)
                        val imageURL: String? = postSnapshot.child("imageURL").getValue(String::class.java)

                        Log.d("ImageURL", "Image URL: $imageURL")

                        // Use the Elvis operator (?:) to provide a default value if imageUrl is null
                        foundPet.imageURL = imageURL ?: "default_image_url"

                        Log.d("ImageURL", "Image URL: ${foundPet.imageURL}")

                        foundPetsList.add(foundPet)
                    }
                }
                foundPetsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })

        foundPetsAdapter.setOnItemClickListener(object : LostAndFoundFoundAdapter.OnItemClickListener {
            override fun onItemClick(foundPet: FoundPet) {
                // Handle item click here, start the LostPetDetails activity
                val intent = Intent(this@LostAndFoundFoundActivity, LostAndFoundFoundPetDetailsActivity::class.java)
                intent.putExtra("foundPet", foundPet) // Pass the selected FoundPet object
                startActivity(intent)
            }
        })

        reportFoundPetButton.setOnClickListener {
            val intent = Intent(this, LostAndFoundFoundFormActivity::class.java)
            startActivity(intent)
        }

    }
}