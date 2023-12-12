package com.example.pawsomepals

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ViewOneVet : AppCompatActivity() {

    private lateinit var vetName : TextView
    private lateinit var degree: TextView
    private lateinit var clinic: TextView
    private lateinit var contact: TextView
    private lateinit var description: TextView
    private lateinit var date1: TextView
    private lateinit var time1: TextView
    private lateinit var date2: TextView
    private lateinit var time2: TextView
    private lateinit var selectedImageUri: ImageView
    private lateinit var updateBtn: Button
    private lateinit var deleteBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_one_vet)

        vetName = findViewById(R.id.vetName)
        degree = findViewById(R.id.degree)
        clinic = findViewById(R.id.clinic)
        contact = findViewById(R.id.contact)
        description = findViewById(R.id.description)
        date1 = findViewById(R.id.date1)
        time1 = findViewById(R.id.time1)
        date2 = findViewById(R.id.date2)
        time2 = findViewById(R.id.time2)
        selectedImageUri = findViewById(R.id.profilePicture)
        updateBtn = findViewById(R.id.updateBtn)
        deleteBtn = findViewById(R.id.deleteBtn)

        initView()
        setValuesToViews()

        deleteBtn.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("uniqueKey").toString()
            )
        }
    }

    private fun deleteRecord(
        uniqueKey: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("vet_details").child(uniqueKey)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Vet data deleted", Toast.LENGTH_LONG).show()
            val intent = Intent(this, FetchingVetInfo::class.java)
            startActivity(intent)
        }.addOnFailureListener {
            Toast.makeText(this, "Deleting Err", Toast.LENGTH_LONG).show()
        }
    }

    private fun initView() {
        vetName = findViewById(R.id.vetName)
        degree = findViewById(R.id.degree)
        clinic = findViewById(R.id.clinic)
        contact = findViewById(R.id.contact)
        description = findViewById(R.id.description)
        date1 = findViewById(R.id.date1)
        time1 = findViewById(R.id.time1)
        date2 = findViewById(R.id.date2)
        time2 = findViewById(R.id.time2)
        selectedImageUri = findViewById(R.id.profilePicture)
        updateBtn = findViewById(R.id.updateBtn)
        deleteBtn = findViewById(R.id.deleteBtn)
    }


    private fun setValuesToViews(){
        vetName.text = intent.getStringExtra("vetName")
        degree.text = intent.getStringExtra("degree")
        clinic.text = intent.getStringExtra("clinic")
        contact.text = intent.getStringExtra("contact")
        description.text = intent.getStringExtra("description")
        date1.text = intent.getStringExtra("date1")
        time1.text = intent.getStringExtra("time1")
        date2.text = intent.getStringExtra("date2")
        time2.text = intent.getStringExtra("time2")
        val imageUrl = intent.getStringExtra("imageURL")
        Picasso.get().load(imageUrl).into(selectedImageUri)
    }
}