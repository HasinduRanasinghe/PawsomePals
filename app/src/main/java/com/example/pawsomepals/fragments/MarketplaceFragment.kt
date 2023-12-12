package com.example.pawsomepals.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawsomepals.ListItemActivity
import com.example.pawsomepals.LoginActivity
import com.example.pawsomepals.R
import com.example.pawsomepals.adapter.MarketplaceAdapter
import com.example.pawsomepals.dataclass.MarketplaceItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MarketplaceFragment : Fragment() {

    private lateinit var marketplaceRecyclerView: RecyclerView
    private var fragmentContext: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    override fun onDetach() {
        super.onDetach()
        fragmentContext = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_marketplace, container, false)

        val packageName = requireContext().packageName
        val preferencesName = "$packageName.user_preferences"
        val sharedPreferences = requireContext().getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        val userID = sharedPreferences.getString("userID","").toString()

        val btnListItem = view.findViewById<Button>(R.id.btnListItem)
        btnListItem.setOnClickListener {
            if(userID.isNullOrBlank() || userID.isNullOrEmpty()){
                Toast.makeText(requireContext(), "Login to list an item", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }else{
                startActivity(Intent(requireContext(), ListItemActivity::class.java))
            }
        }

        marketplaceRecyclerView = view.findViewById(R.id.marketplaceRecycleView)

        val databaseReference = FirebaseDatabase.getInstance().getReference("marketplaceItem")
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<MarketplaceItem>()
                val itemIDs = mutableListOf<String>()
                val isOwner = mutableListOf<Boolean>()
                for (childSnapshot in snapshot.children){
                    val item = childSnapshot.getValue(MarketplaceItem::class.java)
                    val itemID = childSnapshot.key.toString()
                    if(item != null){
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

                marketplaceRecyclerView.adapter = MarketplaceAdapter(items, itemIDs, requireContext(), isOwner)
                marketplaceRecyclerView.layoutManager = GridLayoutManager(context, 2)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }
}