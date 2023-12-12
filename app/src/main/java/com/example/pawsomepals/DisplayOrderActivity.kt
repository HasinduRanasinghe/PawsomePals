package com.example.pawsomepals

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class DisplayOrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_order)

        supportActionBar?.hide()

        val packageName = this.packageName
        val preferencesName = "$packageName.user_preferences"
        val sharedPreferences = this.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        val userID = sharedPreferences.getString("userID","").toString()

        val itemID = intent.getStringExtra("itemID")

        val itemImage = findViewById<ImageView>(R.id.imageViewOrderedItemDisplay)
        val tvItemName = findViewById<TextView>(R.id.tvDisplayOrderTitle)
        val tvQty = findViewById<TextView>(R.id.tvDisplayOrderQty)
        val tvPaymentAmount = findViewById<TextView>(R.id.tvDisplayOrderPaymentAmount)
        val tvDate = findViewById<TextView>(R.id.tvDisplayOrderDate)
        val tvPaymentType = findViewById<TextView>(R.id.tvDisplayOrderPaymentType)

        tvPaymentType.text = "Payment Type: COD"

        val tvBuyerName = findViewById<TextView>(R.id.tvDisplayOrderBuyerName)
        val tvBuyerAddress = findViewById<TextView>(R.id.tvDisplayOrderBuyerAddress)
        val tvBuyerEmail = findViewById<TextView>(R.id.tvDisplayOrderBuyerEmail)
        val tvBuyerPhone = findViewById<TextView>(R.id.tvDisplayOrderBuyerPhone)
        val tvBuyerNote = findViewById<TextView>(R.id.tvDisplayOrderBuyerNote)

        val databaseReference = FirebaseDatabase.getInstance().getReference("user/$userID/receivedOrders/$itemID")
        databaseReference.get().addOnSuccessListener {
            if (it.exists()){
                val itemName = it.child("itemName").value.toString()
                val qty = it.child("qty").value.toString()
                val paymentAmount = it.child("paymentAmount").value.toString()
                val date = it.child("date").value.toString()
                val buyerName = it.child("buyerName").value.toString()
                val address = it.child("address").value.toString()
                val email = it.child("email").value.toString()
                val phone = it.child("phone").value.toString()
                val note = it.child("note").value.toString()
                val itemImageUrl = it.child("itemImage").value.toString()

                tvItemName.text = itemName
                tvQty.text = "Qty: $qty"
                tvPaymentAmount.text = "Payment: $paymentAmount"
                tvDate.text = "Ordered Date: $date"
                tvBuyerName.text = "Name: $buyerName"
                tvBuyerAddress.text = "Address: $address"
                tvBuyerEmail.text = "Email: $email"
                tvBuyerPhone.text = "Contact No.: $phone"
                tvBuyerNote.text = "Note: $note"

                Picasso.get().load(itemImageUrl).into(itemImage)

            }
        }
    }
}