package com.example.pawsomepals

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pawsomepals.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class VetDetailsActivity : AppCompatActivity() {

    private lateinit var vetNameEditText: EditText
    private lateinit var degreeEditText: EditText
    private lateinit var clinicEditText: EditText
    private lateinit var contactEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var date1EditText: EditText
    private lateinit var time1EditText: EditText
    private lateinit var date2EditText: EditText
    private lateinit var time2EditText: EditText
    private lateinit var submitButton: Button
    private lateinit var storageReference: StorageReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var selectedImageUri: Uri
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vet_details)

        vetNameEditText = findViewById(R.id.vetName)
        degreeEditText = findViewById(R.id.degree)
        clinicEditText = findViewById(R.id.clinic)
        contactEditText = findViewById(R.id.contact)
        descriptionEditText = findViewById(R.id.description)
        date1EditText = findViewById(R.id.date1)
        time1EditText = findViewById(R.id.time1)
        date2EditText = findViewById(R.id.date2)
        time2EditText = findViewById(R.id.time2)
        submitButton = findViewById(R.id.btnSubmit)
        storageReference = FirebaseStorage.getInstance().reference
        firebaseDatabase = FirebaseDatabase.getInstance()

        // Set an OnClickListener for the ImageView to handle image uploads
        val imageView: ImageView = findViewById(R.id.profilePicture)
        imageView.setOnClickListener {
            openImagePicker()
        }

        submitButton.setOnClickListener {
            if (validate()) {
                saveDataToFirebase()
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data!!
            // You can set the ImageView to display the selected image here
            val imageView: ImageView = findViewById(R.id.profilePicture)
            imageView.setImageURI(selectedImageUri)
        }
    }

    private fun validate(): Boolean {
        val vetName = vetNameEditText.text.toString()
        val degree = degreeEditText.text.toString()
        val clinic = clinicEditText.text.toString()
        val contact = contactEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val date1 = date1EditText.text.toString()
        val time1 = time2EditText.text.toString()
        val date2 = date2EditText.text.toString()
        val time2 = time2EditText.text.toString()


        // Check if any of the fields are empty
        if (vetName.isEmpty() || degree.isEmpty() || clinic.isEmpty() || contact.isEmpty() || description.isEmpty() || date1.isEmpty() || time1.isEmpty() || date2.isEmpty() || time2.isEmpty() || selectedImageUri == null) {
            // Display a Toast message to notify the user
            Toast.makeText(
                this, "Please complete all fields and select an image.", Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true
    }

    private fun saveDataToFirebase() {
        val vetName = vetNameEditText.text.toString()
        val degree = degreeEditText.text.toString()
        val clinic = clinicEditText.text.toString()
        val contact = contactEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val date1 = date1EditText.text.toString()
        val time1 = time2EditText.text.toString()
        val date2 = date2EditText.text.toString()
        val time2 = time2EditText.text.toString()

        val databaseReference = firebaseDatabase.getReference("vet_details")

        val uniqueKey = databaseReference.push().key // Generate a unique key

        // Create a map with the data you want to save
        val data = mapOf(
            "vetName" to vetName,
            "degree" to degree,
            "clinic" to clinic,
            "contact" to contact,
            "description" to description,
            "date1" to date1,
            "time1" to time1,
            "date2" to date2,
            "time2" to time2,
        )

        if (uniqueKey != null) {
            databaseReference.child(uniqueKey).setValue(data)
            uploadImageToStorage(uniqueKey)
        } else {
            // Display a Toast message if the unique key couldn't be generated
            Toast.makeText(applicationContext, "Failed to save data. Please try again.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun uploadImageToStorage(uniqueKey: String) {
        val imageRef = storageReference.child("vet_images/$uniqueKey.jpg")
        imageRef.putFile(selectedImageUri).addOnSuccessListener { taskSnapshot ->
            // Handle successful image upload
            // Get the download URL of the uploaded image
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // Save the image URL in the database
                saveImageUrlToDatabase(uniqueKey, uri.toString())
                // Display a success Toast message
                Toast.makeText(applicationContext, "Report Saved Successfully", Toast.LENGTH_SHORT).show()

                fun navigateToNextActivity() {
                    val intent = Intent(this, FetchingVetInfo::class.java)
                    startActivity(intent)
                }

                navigateToNextActivity()
            }
        }.addOnFailureListener { exception ->
            // Display an error Toast message if image upload fails
            Toast.makeText(
                applicationContext, "Failed to upload image. Please try again.", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun saveImageUrlToDatabase(uniqueKey: String, imageUrl: String) {
        val databaseReference = firebaseDatabase.getReference("vet_details")
        val imageRef = databaseReference.child(uniqueKey).child("imageURL")
        imageRef.setValue(imageUrl)
    }
}
