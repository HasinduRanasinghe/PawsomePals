package com.example.pawsomepals

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class FetchingVetInfo : AppCompatActivity() {

    private lateinit var vetsRecyclerView: RecyclerView
    private lateinit var vetsList:ArrayList<Vet>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching_vet_info)

        vetsRecyclerView = findViewById(R.id.vetView)
        vetsRecyclerView.layoutManager = LinearLayoutManager(this)
        vetsRecyclerView.setHasFixedSize(true)

        vetsList = arrayListOf<Vet>()
        getVetsData()

        val addVetButton: Button = findViewById(R.id.addVet)

        fun navigateToAddVetActivity() {
            val intent = Intent(this, VetDetailsActivity::class.java)
            startActivity(intent)
        }

        addVetButton.setOnClickListener {
            navigateToAddVetActivity()
        }
    }

    private fun getVetsData(){
        vetsRecyclerView.visibility = View.GONE

        dbRef = FirebaseDatabase.getInstance().getReference("vet_details")

        dbRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {
                vetsList.clear()
                if(snapshot.exists()){
                    for(vetSnap in snapshot.children) {
                        val vetsData = vetSnap.getValue(Vet::class.java)


                        vetsList.add(vetsData!!)


                    }
                }
                val pAdapter = VetAdapter(vetsList)


                pAdapter.setItemClickListner(object : VetAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {

                        val intent = Intent(this@FetchingVetInfo, ViewOneVet::class.java)

                        //put extra
                        intent.putExtra("vetName",vetsList[position].vetName)
                        intent.putExtra("degree", vetsList[position].degree)
                        intent.putExtra("clinic", vetsList[position].clinic)
                        intent.putExtra("contact", vetsList[position].contact)
                        intent.putExtra("description", vetsList[position].description)
                        intent.putExtra("date1", vetsList[position].date1)
                        intent.putExtra("time1", vetsList[position].time1)
                        intent.putExtra("date2", vetsList[position].date2)
                        intent.putExtra("time2", vetsList[position].time2)
                        intent.putExtra("imageURL", vetsList[position].imageURL)

                        startActivity(intent)
                    }

                })
                vetsRecyclerView.adapter = pAdapter
                vetsRecyclerView.visibility = View.VISIBLE
            }


            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
}
