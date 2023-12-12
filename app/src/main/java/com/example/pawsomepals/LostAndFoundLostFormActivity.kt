package com.example.pawsomepals

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class LostAndFoundLostFormActivity : AppCompatActivity() {

    private lateinit var petNameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var breedEditText: EditText
    private lateinit var typeEditText: EditText
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var descEditText: EditText
    private lateinit var ownerNameEditText: EditText
    private lateinit var cNumEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var reportButton: Button
    private lateinit var storageReference: StorageReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var selectedImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lost_and_found_lost_form)

        petNameEditText = findViewById(R.id.et_lostRForm_PetName)
        ageEditText = findViewById(R.id.et_lostRForm_Age)
        breedEditText = findViewById(R.id.et_lostRForm_Breed)
        typeEditText = findViewById(R.id.et_lostRForm_Type)
        genderRadioGroup = findViewById(R.id.rg_gender)
        descEditText = findViewById(R.id.et_lostRForm_Desc)
        ownerNameEditText = findViewById(R.id.et_lostRForm_OwnerName)
        cNumEditText = findViewById(R.id.et_lostRForm_CNum)
        locationEditText = findViewById(R.id.et_lostRForm_Location)
        reportButton = findViewById(R.id.btn_lostRForm)
        storageReference = FirebaseStorage.getInstance().reference
        firebaseDatabase = FirebaseDatabase.getInstance()

        // Set an OnClickListener for the ImageView to handle image uploads
        val imageView: ImageView = findViewById(R.id.iv_lostRF)
        imageView.setOnClickListener {
            openImagePicker()
        }

        reportButton.setOnClickListener {
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
            val imageView: ImageView = findViewById(R.id.iv_lostRF)
            imageView.setImageURI(selectedImageUri)
        }
    }

    private fun validate(): Boolean {
        val petName = petNameEditText.text.toString()
        val age = ageEditText.text.toString()
        val breed = breedEditText.text.toString()
        val type = typeEditText.text.toString()
        val genderRadioButtonId = genderRadioGroup.checkedRadioButtonId
        val description = descEditText.text.toString()
        val ownerName = ownerNameEditText.text.toString()
        val contactNumber = cNumEditText.text.toString()
        val location = locationEditText.text.toString()

        // Check if any of the fields are empty
        if (petName.isEmpty() || age.isEmpty() || breed.isEmpty() || type.isEmpty() || genderRadioButtonId == -1 || description.isEmpty() || ownerName.isEmpty() || contactNumber.isEmpty() || location.isEmpty() || selectedImageUri == null) {
            // Display a Toast message to notify the user
            Toast.makeText(
                this, "Please complete all fields and select an image.", Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true
    }

    private fun saveDataToFirebase() {
        val petName = petNameEditText.text.toString()
        val age = ageEditText.text.toString()
        val breed = breedEditText.text.toString()
        val type = typeEditText.text.toString()
        val genderRadioButton = findViewById<RadioButton>(genderRadioGroup.checkedRadioButtonId)
        val gender = genderRadioButton.text.toString()
        val description = descEditText.text.toString()
        val ownerName = ownerNameEditText.text.toString()
        val contactNumber = cNumEditText.text.toString()
        val location = locationEditText.text.toString()

        val databaseReference = firebaseDatabase.getReference("lost_and_found_lost")

        val uniqueKey = databaseReference.push().key // Generate a unique key

        // Create a map with the data you want to save
        val data = mapOf(
            "petName" to petName,
            "age" to age,
            "breed" to breed,
            "type" to type,
            "gender" to gender,
            "description" to description,
            "ownerName" to ownerName,
            "contactNumber" to contactNumber,
            "location" to location
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
        val imageRef = storageReference.child("lost_images/$uniqueKey.jpg")
        imageRef.putFile(selectedImageUri).addOnSuccessListener { taskSnapshot ->
                // Handle successful image upload
                // Get the download URL of the uploaded image
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Save the image URL in the database
                    saveImageUrlToDatabase(uniqueKey, uri.toString())
                    // Display a success Toast message
                    Toast.makeText(applicationContext, "Report Saved Successfully", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->
                // Display an error Toast message if image upload fails
                Toast.makeText(
                    applicationContext, "Failed to upload image. Please try again.", Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun saveImageUrlToDatabase(uniqueKey: String, imageUrl: String) {
        val databaseReference = firebaseDatabase.getReference("lost_and_found_lost")
        val imageRef = databaseReference.child(uniqueKey).child("imageURL")
        imageRef.setValue(imageUrl)
    }
}
