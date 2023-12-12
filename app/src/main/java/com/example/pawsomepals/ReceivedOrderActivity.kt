package com.example.pawsomepals

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawsomepals.adapter.MarketplaceAdapter
import com.example.pawsomepals.adapter.ReceivedOrderAdapter
import com.example.pawsomepals.dataclass.MarketplaceItem
import com.example.pawsomepals.dataclass.RecievedOrder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReceivedOrderActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_received_order)

        supportActionBar?.hide()

        val packageName = this.packageName
        val preferencesName = "$packageName.user_preferences"
        val sharedPreferences = this.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        val userID = sharedPreferences.getString("userID","").toString()

        recyclerView = findViewById(R.id.recycleViewReceivedOrder)

        val databaseReference = FirebaseDatabase.getInstance().getReference("user/$userID/receivedOrders")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<RecievedOrder>()
                val itemIDs = mutableListOf<String>()
                for (childSnapshot in snapshot.children){
                    val item = childSnapshot.getValue(RecievedOrder::class.java)
                    val itemID = childSnapshot.key.toString()
                    if(item != null){
                        items.add(item)
                    }
                    if(itemID != null){
                        itemIDs.add(itemID)
                    }
                }

                recyclerView.adapter = ReceivedOrderAdapter(items, itemIDs, this@ReceivedOrderActivity)
                recyclerView.layoutManager = LinearLayoutManager(this@ReceivedOrderActivity)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ReceivedOrderActivity,error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}