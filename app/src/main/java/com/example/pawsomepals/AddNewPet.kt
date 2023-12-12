package com.example.pawsomepals

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import com.example.pawsomepals.databinding.ActivityAddNewPetBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddNewPet : AppCompatActivity() {

    private lateinit var petName : EditText
    private lateinit var breed: EditText
    private lateinit var age: EditText
    private lateinit var petDescription: EditText
    private lateinit var organizationName: EditText
    private lateinit var address: EditText
    private lateinit var contact: EditText
    private lateinit var btnAdd: Button
    private var gender: String = ""
    private var PetCategory: String = ""
    private lateinit var selectedImageUri: Uri
    private lateinit var storageReference: StorageReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_pet)


        // Accessing the views from XML using findViewById
        petName = findViewById(R.id.petName)
        val radioGroup: RadioGroup = findViewById(R.id.radioGroup)
        val radioButton1: RadioButton = findViewById(R.id.radioButton1)
        val radioButton2: RadioButton = findViewById(R.id.radioButton2)
        dbRef = FirebaseDatabase.getInstance().getReference("Pets")

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton: RadioButton = findViewById(checkedId)
            PetCategory = radioButton.text.toString()
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton: RadioButton = findViewById(checkedId)
            gender = radioButton.text.toString()
        }


        breed = findViewById(R.id.breed)
        age = findViewById(R.id.age)


        petDescription = findViewById(R.id.petDescription)
        organizationName = findViewById(R.id.Owner_organization_name)
        address = findViewById(R.id.Address)
        contact = findViewById(R.id.contact)
        btnAdd = findViewById(R.id.btnAdd)
        storageReference = FirebaseStorage.getInstance().reference
        firebaseDatabase = FirebaseDatabase.getInstance()
        btnAdd.setOnClickListener{
            savePetInformation()
        }

        val imageView: ImageView = findViewById(R.id.petImageView)
        imageView.setOnClickListener {
            openImagePicker()
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
            val imageView: ImageView = findViewById(R.id.petImageView)
            imageView.setImageURI(selectedImageUri)
        }
    }




    private fun savePetInformation(){
        //getting values
        val petNameValue = petName.text.toString()
        val breedValue = breed.text.toString()
        val ageValue = age.text.toString()
        val petDescriptionValue = petDescription.text.toString()
        val organizationNameValue = organizationName.text.toString()
        val addressValue = address.text.toString()
        val contactValue = contact.text.toString()

        if(petNameValue.isEmpty()){
            petName.error = "Please enter name"
        }
        if (breedValue.isEmpty()) {
            breed.error = "Please enter breed"
        }
        if (ageValue.isEmpty()) {
            age.error = "Please enter age"
        }
        if (petDescriptionValue.isEmpty()) {
            age.error = "Please enter description"
        }
        if (organizationNameValue.isEmpty()) {
            organizationName.error = "Please enter organization name"
        }
        if (addressValue.isEmpty()) {
            address.error = "Please enter address"
        }
        if (contactValue.isEmpty()) {
            contact.error = "Please enter contact details"
        }

        val petId = dbRef.push().key!!

        val pet = PetInformationData(
            petId,
            petNameValue,
            gender,
            PetCategory,
            breedValue,
            ageValue,
            petDescriptionValue,
            organizationNameValue,
            addressValue,
            contactValue
        )

        val data = mapOf(
            "petId" to petId,
            "petName" to petNameValue,
            "breed" to breedValue,
            "age" to ageValue,
            "petDescription" to petDescriptionValue,
            "organizationName" to organizationNameValue,
            "address" to addressValue,
            "contact" to contactValue,
            "gender" to gender
        )

        if (petId != null) {
            dbRef.child(petId).setValue(data)
            uploadImageToStorage(petId)
        } else {
            // Display a Toast message if the petId couldn't be generated
            Toast.makeText(this, "Failed to save data. Please try again.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImageToStorage(uniqueKey: String) {
        val imageRef = storageReference.child("pet_images/$uniqueKey.jpg")
        imageRef.putFile(selectedImageUri).addOnSuccessListener { taskSnapshot ->
            // Handle successful image upload
            // Get the download URL of the uploaded image
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // Save the image URL in the database
                saveImageUrlToDatabase(uniqueKey, uri.toString())
                // Display a success Toast message
                Toast.makeText(applicationContext, "Data inserted successfully", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            // Display an error Toast message if image upload fails
            Toast.makeText(
                applicationContext, "Failed to upload image. Please try again.", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun saveImageUrlToDatabase(uniqueKey: String, imageUrl: String) {
        val databaseReference = firebaseDatabase.getReference("Pets")
        val imageRef = databaseReference.child(uniqueKey).child("imageURL")
        imageRef.setValue(imageUrl)
    }
}