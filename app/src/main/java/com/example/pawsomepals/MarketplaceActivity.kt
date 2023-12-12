package com.example.pawsomepals

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawsomepals.adapter.MarketplaceAdapter
import com.example.pawsomepals.dataclass.MarketplaceItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MarketplaceActivity : AppCompatActivity() {

    private lateinit var marketplaceRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marketplace)

        supportActionBar?.hide()

        val btnReceivedOrder = findViewById<Button>(R.id.btnReceivedOrder)
        btnReceivedOrder.setOnClickListener {
            startActivity(Intent(this@MarketplaceActivity, ReceivedOrderActivity::class.java))
        }

        val packageName = this.packageName
        val preferencesName = "$packageName.user_preferences"
        val sharedPreferences = this.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        val userID = sharedPreferences.getString("userID","").toString()

        val sellerID = intent.getStringExtra("sellerID")

        val databaseReferenceUser = FirebaseDatabase.getInstance().getReference("user")
        if (sellerID != null) {
            databaseReferenceUser.child(sellerID).get().addOnSuccessListener {
                if (it.exists()){
                    val sellerName = it.child("name").value.toString()
                    val sellerEmail = it.child("email").value.toString()
                    val sellerPhone = it.child("phone").value.toString()

                    findViewById<TextView>(R.id.tvHeaderMarketplaceProfile).text = sellerName
                    findViewById<TextView>(R.id.tvMarketplaceProfileUserName).text = sellerName
                    findViewById<TextView>(R.id.tvMarketplaceProfileEmail).text = sellerEmail
                    findViewById<TextView>(R.id.tvMarketplaceProfilePhone).text = sellerPhone
                }
            }
        }

        if (sellerID == userID){
            var btnEditProfile = findViewById<Button>(R.id.btnEditMarketplaceProfile)
            btnEditProfile.visibility = View.VISIBLE

            var btnContainer = findViewById<LinearLayout>(R.id.receivedOrderBtnContainer)
            btnContainer.visibility = View.VISIBLE
        }

        marketplaceRecyclerView = findViewById(R.id.recycleViewMarketplaceProfile)

        val databaseReference = FirebaseDatabase.getInstance().getReference("marketplaceItem")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<MarketplaceItem>()
                val itemIDs = mutableListOf<String>()
                val isOwner = mutableListOf<Boolean>()
                for (childSnapshot in snapshot.children){
                    val item = childSnapshot.getValue(MarketplaceItem::class.java)
                    val itemID = childSnapshot.key.toString()

                    if(item != null){
                        if(item.owner == sellerID){
                            items.add(item)
                        }

                        if(itemID != null){
                            itemIDs.add(itemID)
                        }
                        if (item != null) {
                            when(item.owner.toString()){
                                userID -> isOwner.add(true)
                                else -> isOwner.add(false)
                            }
                        }
                    }
                }

                marketplaceRecyclerView.adapter = MarketplaceAdapter(items, itemIDs, this@MarketplaceActivity, isOwner)
                marketplaceRecyclerView.layoutManager = GridLayoutManager(this@MarketplaceActivity, 2)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MarketplaceActivity,error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}