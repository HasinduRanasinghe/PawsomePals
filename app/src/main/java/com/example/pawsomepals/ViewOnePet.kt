package com.example.pawsomepals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ViewOnePet : AppCompatActivity() {

    private lateinit var petName: TextView
    private lateinit var breed: TextView
    private lateinit var age: TextView
    private lateinit var petDescription: TextView
    private lateinit var organizationName: TextView
    private lateinit var address: TextView
    private lateinit var contact: TextView
    private lateinit var gender: TextView
    private lateinit var PetCategory: TextView
    private lateinit var selectedImageUri: ImageView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_one_pet)

        petName = findViewById(R.id.petName)
        breed = findViewById(R.id.breed)
        age = findViewById(R.id.age)
        petDescription = findViewById(R.id.petDescription)
        organizationName = findViewById(R.id.ownerOrganizationName)
        address = findViewById(R.id.address)
        contact = findViewById(R.id.contact)
        gender = findViewById(R.id.gender)
        selectedImageUri = findViewById(R.id.petImage)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdatingDialog(
                intent.getStringExtra("petId").toString(),
                intent.getStringExtra("petName").toString(),
                intent.getStringExtra("breed").toString(),
                intent.getStringExtra("age").toString(),
                intent.getStringExtra("petDescription").toString(),
                intent.getStringExtra("organizationName").toString(),
                intent.getStringExtra("address").toString(),
                intent.getStringExtra("contact").toString(),

                )
        }
        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("petId").toString()
            )
        }
    }

    private fun deleteRecord(
        petId: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Pets").child(petId)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Pet data deleted", Toast.LENGTH_LONG).show()
            val intent = Intent(this, FetchingPetsInfor::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener {
            Toast.makeText(this, "Deleting Err", Toast.LENGTH_LONG).show()
        }
    }

    private fun initView() {
        petName = findViewById(R.id.petName)
        breed = findViewById(R.id.breed)
        age = findViewById(R.id.age)
        petDescription = findViewById(R.id.petDescription)
        organizationName = findViewById(R.id.ownerOrganizationName)
        address = findViewById(R.id.address)
        contact = findViewById(R.id.contact)
        gender = findViewById(R.id.gender)
        // PetCategory = findViewById(R.id.petca)
        selectedImageUri = findViewById(R.id.petImage)
    }

    private fun setValuesToViews() {


        petName.text = intent.getStringExtra("petName")
        breed.text = intent.getStringExtra("breed")
        age.text = intent.getStringExtra("age")
        petDescription.text = intent.getStringExtra("petDescription")
        organizationName.text = intent.getStringExtra("organizationName")
        address.text = intent.getStringExtra("address")
        contact.text = intent.getStringExtra("contact")
        //     gender.text = intent.getStringExtra("gender")
//        PetCategory.text = intent.getStringExtra("PetCategory")
        val imageUrl = intent.getStringExtra("imageURL")
        Picasso.get().load(imageUrl).into(selectedImageUri)


    }

    private fun openUpdatingDialog(
        petId: String,
        petName: String,
        breed: String,
        age: String,
        petDescription: String,
        organizationName: String,
        address: String,
        contact: String,

        ) {
        val pDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val pDialogView = inflater.inflate(R.layout.update_pets_infor, null)

        pDialog.setView(pDialogView)

        val etPetName = pDialogView.findViewById<EditText>(R.id.petName)
        //val etPetCategory = pDialogView.findViewById<EditText>(R.id.PetCategory)
        val etBreed = pDialogView.findViewById<EditText>(R.id.breed)
        val etAge = pDialogView.findViewById<EditText>(R.id.age)
//        val etGender = pDialogView.findViewById<EditText>(R.id.petName)
        val etPetDescription = pDialogView.findViewById<EditText>(R.id.petDescription)
        val etOwnerOrganizationName = pDialogView.findViewById<EditText>(R.id.ownerOrganizationName)
        val etAddress = pDialogView.findViewById<EditText>(R.id.Address)
        val etContact = pDialogView.findViewById<EditText>(R.id.contact)
        val btnUpdate = pDialogView.findViewById<Button>(R.id.btnUpdate)

        etPetName.setText(intent.getStringExtra("petName"))
        //etPetCategory.setText(intent.getStringExtra("petCategory"))
        etBreed.setText(intent.getStringExtra("breed"))
        etAge.setText(intent.getStringExtra("age"))
        //etGender.setText(intent.getStringExtra("gender"))
        etPetDescription.setText(intent.getStringExtra("petDescription"))
//        etOwnerOrganization.setText(intent.getStringExtra("ownerOrganization"))
        etOwnerOrganizationName.setText(intent.getStringExtra("ownerOrganizationName"))
        etAddress.setText(intent.getStringExtra("address"))
        etContact.setText(intent.getStringExtra("contact"))

        pDialog.setTitle("Updating $petName's Record")

        val alertDialog = pDialog.create()
        alertDialog.show()


        btnUpdate.setOnClickListener {
            updatePetData(
                petId,
                etPetName.text.toString(),
                //etPetCategory.text.toString(),
                etBreed.text.toString(),
                etAge.text.toString(),
                //etGender.text.toString(),
                etPetDescription.text.toString(),

                etOwnerOrganizationName.text.toString(),
                etAddress.text.toString(),
                etContact.text.toString()

            )
            Toast.makeText(applicationContext, "Pet Data Updated", Toast.LENGTH_LONG).show()
            this@ViewOnePet.petName.text = etPetName.text.toString()
            this@ViewOnePet.breed.text = etBreed.text.toString()
            this@ViewOnePet.age.text = etAge.text.toString()
            this@ViewOnePet.petDescription.text = etPetDescription.text.toString()
            this@ViewOnePet.organizationName.text = etOwnerOrganizationName.text.toString()
            this@ViewOnePet.address.text = etAddress.text.toString()
            this@ViewOnePet.contact.text = etContact.text.toString()

            alertDialog.dismiss()
        }


//        petName.text = etPetName.text.toString()
//        breed.text = etBreed.text.toString()
//        age.text = etAge.text.toString()
//        petDescription.text = etPetDescription.text.toString()
//        organizationName.text = etOwnerOrganization.text.toString()
//        address.text = etOwnerOrganizationName.text.toString()
//        contact.text = etContact.text.toString()


    }

    private fun updatePetData(
        petId: String,
        pName: String,
        pBreed: String,
        pAge: String,
        pDescription: String,
        pOwnerOrganizationName: String,
        pAddress: String,
        pContact: String,

        ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Pets").child(petId)
//        val petInfo = PetInformationData(
//            petId = petId,
//            petName = pName,
//            breed = pBreed,
//            age = pAge,
//            petDescription = pDescription,
//            ownerOrganizationName = pOwnerOrganizationName,
//            address = pAddress,
//            contact = pContact
//        )
//        dbRef.setValue(petInfo)
        val updates: MutableMap<String, Any> = HashMap()
        updates["petName"] = pName
        updates["breed"] = pBreed
        updates["age"] = pAge
        updates["petDescription"] = pDescription
        updates["ownerOrganizationName"] = pOwnerOrganizationName
        updates["address"] = pAddress
        updates["contact"] = pContact

        dbRef.updateChildren(updates)
    }
}