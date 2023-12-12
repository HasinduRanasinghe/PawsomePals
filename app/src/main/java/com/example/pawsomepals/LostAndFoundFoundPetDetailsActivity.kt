package com.example.pawsomepals

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class LostAndFoundFoundPetDetailsActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lost_and_found_found_pet_details)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("owner_details")

        val notifyFounderButton = findViewById<Button>(R.id.btn_notifyFounder)

        // Receive the LostPet object from the intent
        val foundPet = intent.getParcelableExtra<FoundPet>("foundPet")

        // Access your UI elements and display the details
        val petImageView = findViewById<ImageView>(R.id.iv_foundPetDetails)
        val petNameTextView = findViewById<TextView>(R.id.tv_foundPetDetailsPName)
        val locationTextView = findViewById<TextView>(R.id.tv_foundPetDetailsLLoc)
        val typeTextView = findViewById<TextView>(R.id.tv_foundPetDetailsType)
        val breedNameTextView = findViewById<TextView>(R.id.tv_foundPetDetailsBreedName)
        val ageNameTextView = findViewById<TextView>(R.id.tv_foundPetDetailsAgeNumber)
        val genderTypeTextView = findViewById<TextView>(R.id.tv_foundPetDetailsGenderType)
        val descriptionTextView = findViewById<TextView>(R.id.tv_foundPetDetailsDescription)
        val founderNameTextView = findViewById<TextView>(R.id.tv_foundPetDetailsOwnerName)
        val founderContactTextView = findViewById<TextView>(R.id.tv_foundPetDetailsOwnerContact)
        val locationInfoTextView = findViewById<TextView>(R.id.tv_foundPetDetailsLocation)

        // Check if foundPet is not null
        if (foundPet != null) {
            Picasso.get().load(foundPet.imageURL).into(petImageView)

            petNameTextView.text = foundPet.petName
            locationTextView.text = foundPet.location
            typeTextView.text = foundPet.type
            breedNameTextView.text = foundPet.breed
            ageNameTextView.text = foundPet.age
            genderTypeTextView.text = foundPet.gender
            descriptionTextView.text = foundPet.description
            founderNameTextView.text = foundPet.founderName
            founderContactTextView.text = foundPet.contactNumber
            locationInfoTextView.text = foundPet.location
        }

        notifyFounderButton.setOnClickListener {
            // Create a custom dialog
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.notify_founder_popup)

            // Find the form elements within the dialog
            val ownerNameEditText = dialog.findViewById<EditText>(R.id.et_ownerName)
            val contactNumberEditText = dialog.findViewById<EditText>(R.id.et_contactNumber)
            val descriptionEditText = dialog.findViewById<EditText>(R.id.et_description)
            val locationEditText = dialog.findViewById<EditText>(R.id.et_location)
            val sendNotificationButton = dialog.findViewById<Button>(R.id.btn_sendNotification)

            // Set onClickListener for the "Send Notification" button within the dialog
            sendNotificationButton.setOnClickListener {
                // Retrieve the data entered by the user
                val ownerName = ownerNameEditText.text.toString()
                val contactNumber = contactNumberEditText.text.toString()
                val description = descriptionEditText.text.toString()
                val location = locationEditText.text.toString()

                val notificationData = HashMap<String, Any>()
                notificationData["ownerName"] = ownerName
                notificationData["contactNumber"] = contactNumber
                notificationData["description"] = description
                notificationData["location"] = location

                // Save data to Firebase
                val notificationKey = databaseReference.push().key
                if (notificationKey != null) {
                    databaseReference.child(notificationKey).setValue(notificationData).addOnSuccessListener {
                        Toast.makeText(applicationContext, "Your Details Saved Successfully", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Saving Unsuccessful", Toast.LENGTH_SHORT).show()
                }

                dialog.dismiss()
            }

            dialog.show()
        }

    }
}