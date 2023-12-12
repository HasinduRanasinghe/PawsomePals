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

class LostAndFoundLostPetDetailsActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lost_and_found_lost_pet_details)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("founder_details")

        val notifyOwnerButton = findViewById<Button>(R.id.btn_notifyOwner)

        // Receive the LostPet object from the intent
        val lostPet = intent.getParcelableExtra<LostPet>("lostPet")

        // Access your UI elements and display the details
        val petImageView = findViewById<ImageView>(R.id.iv_lostPetDetails)
        val petNameTextView = findViewById<TextView>(R.id.tv_lostPetDetailsPName)
        val locationTextView = findViewById<TextView>(R.id.tv_lostPetDetailsLLoc)
        val typeTextView = findViewById<TextView>(R.id.tv_lostPetDetailsType)
        val breedNameTextView = findViewById<TextView>(R.id.tv_lostPetDetailsBreedName)
        val ageNameTextView = findViewById<TextView>(R.id.tv_lostPetDetailsAgeNumber)
        val genderTypeTextView = findViewById<TextView>(R.id.tv_lostPetDetailsGenderType)
        val descriptionTextView = findViewById<TextView>(R.id.tv_lostPetDetailsDescription)
        val ownerNameTextView = findViewById<TextView>(R.id.tv_lostPetDetailsOwnerName)
        val ownerContactTextView = findViewById<TextView>(R.id.tv_lostPetDetailsOwnerContact)
        val locationInfoTextView = findViewById<TextView>(R.id.tv_lostPetDetailsLocation)

        // Check if lostPet is not null
        if (lostPet != null) {
            Picasso.get().load(lostPet.imageURL).into(petImageView)

            petNameTextView.text = lostPet.petName
            locationTextView.text = lostPet.location
            typeTextView.text = lostPet.type
            breedNameTextView.text = lostPet.breed
            ageNameTextView.text = lostPet.age
            genderTypeTextView.text = lostPet.gender
            descriptionTextView.text = lostPet.description
            ownerNameTextView.text = lostPet.ownerName
            ownerContactTextView.text = lostPet.contactNumber
            locationInfoTextView.text = lostPet.location
        }

        notifyOwnerButton.setOnClickListener {
            // Create a custom dialog
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.notify_owner_popup)

            // Find the form elements within the dialog
            val founderNameEditText = dialog.findViewById<EditText>(R.id.et_founderName)
            val contactNumberEditText = dialog.findViewById<EditText>(R.id.et_contactNumber)
            val descriptionEditText = dialog.findViewById<EditText>(R.id.et_description)
            val locationEditText = dialog.findViewById<EditText>(R.id.et_location)
            val sendNotificationButton = dialog.findViewById<Button>(R.id.btn_sendNotification)

            // Set onClickListener for the "Send Notification" button within the dialog
            sendNotificationButton.setOnClickListener {
                // Retrieve the data entered by the user
                val founderName = founderNameEditText.text.toString()
                val contactNumber = contactNumberEditText.text.toString()
                val description = descriptionEditText.text.toString()
                val location = locationEditText.text.toString()

                val notificationData = HashMap<String, Any>()
                notificationData["founderName"] = founderName
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
