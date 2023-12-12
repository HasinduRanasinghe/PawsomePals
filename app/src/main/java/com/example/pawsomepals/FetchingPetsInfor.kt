package com.example.pawsomepals

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class FetchingPetsInfor : AppCompatActivity() {
    private lateinit var petsRecyclerView: RecyclerView
    private lateinit var petsList:ArrayList<PetInformationData>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching_pets_infor)

        petsRecyclerView = findViewById(R.id.petsView2)
        petsRecyclerView.layoutManager = LinearLayoutManager(this)
        petsRecyclerView.setHasFixedSize(true)

        petsList = arrayListOf<PetInformationData>()
        getPetsData()

        val addPetButton = findViewById<Button>(R.id.addPetButton)
        addPetButton.setOnClickListener {
            val intent = Intent(this, AddNewPet::class.java)
            startActivity(intent)
        }
    }

    private fun getPetsData(){
        petsRecyclerView.visibility = View.GONE

        dbRef = FirebaseDatabase.getInstance().getReference("Pets")

        dbRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {
                petsList.clear()
                if(snapshot.exists()){
                    for(petSnap in snapshot.children) {
                        val petsData = petSnap.getValue(PetInformationData::class.java)


                            petsList.add(petsData!!)


                        }
                    }
                    val pAdapter = PetsAdapter(petsList)
                    petsRecyclerView.adapter = pAdapter

                pAdapter.setItemClickListner(object : PetsAdapter.onIteamClickListner{
                    override fun onItemClick(posiion: Int) {

                        val intent = Intent(this@FetchingPetsInfor, ViewOnePet::class.java)

                        //put extra
                        intent.putExtra("petName",petsList[posiion].petName)
                        intent.putExtra("breed",petsList[posiion].breed)
                        intent.putExtra("PetCategory", petsList[posiion].PetCategory)
                        intent.putExtra("age", petsList[posiion].age)
                        intent.putExtra("gender", petsList[posiion].gender)
                        intent.putExtra("petDescription", petsList[posiion].petDescription)
                        intent.putExtra("ownerOrganizationName", petsList[posiion].organizationName)
                        intent.putExtra("address", petsList[posiion].address)
                        intent.putExtra("contact", petsList[posiion].contact)
                        intent.putExtra("imageURL", petsList[posiion].imageURL)

                        startActivity(intent)
                    }

                })
                    petsRecyclerView.visibility = View.VISIBLE
                }


            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
}