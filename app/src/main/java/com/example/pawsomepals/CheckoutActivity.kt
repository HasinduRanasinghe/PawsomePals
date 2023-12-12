package com.example.pawsomepals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.pawsomepals.dataclass.RecievedOrder
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import java.util.*

class CheckoutActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        supportActionBar?.hide()

        val itemID = intent.getStringExtra("itemID")
        val itemName = intent.getStringExtra("itemName")
        val itemPrice = intent.getStringExtra("itemPrice")
        val itemDiscount = intent.getStringExtra("itemDiscount")
        val itemImage = intent.getStringExtra("itemImage")
        val itemOwner = intent.getStringExtra("itemOwner")
        val userName = intent.getStringExtra("userName")
        val userID = intent.getStringExtra("userID")

        val editTextAddress = findViewById<EditText>(R.id.editTextCheckoutAddress)
        val editTextEmail = findViewById<EditText>(R.id.editTextCheckoutEmail)
        val editTextPhone = findViewById<EditText>(R.id.editTextCheckoutPhone)
        val editTextNote = findViewById<EditText>(R.id.editTextCheckoutNote)

        val imageViewItemImage = findViewById<ImageView>(R.id.imageViewItemImage)
        Picasso.get().load(itemImage).into(imageViewItemImage)

        val tvTitle = findViewById<TextView>(R.id.tvCheckoutItemName)
        val tvPrice = findViewById<TextView>(R.id.tvCheckoutItemPrice)

        tvTitle.text = itemName
        tvPrice.text = itemPrice

        var qty = 0
        var price = itemPrice?.toDouble()
        var subtotal = 0.0
        var total = 0.0
        var discountPercentage = itemDiscount?.toDouble()
        var discount = price?.times(discountPercentage!!)?.div(100)

        val tvQty = findViewById<TextView>(R.id.tvCheckoutItemQty)
        tvQty.text = "Qty: 0"
        val tvQtyCounter = findViewById<TextView>(R.id.tvCheckoutItemQtyCounter)
        tvQtyCounter.text = "0"
        val imageViewPlus = findViewById<ImageView>(R.id.imageViewPlusQty)
        val imageViewMinus = findViewById<ImageView>(R.id.imageViewMinusQty)

        val tvSubtotal = findViewById<TextView>(R.id.tvSubtotalCheckout)
        tvSubtotal.text = "0"
        val tvDiscount = findViewById<TextView>(R.id.tvDiscountCheckout)
        tvDiscount.text = "$discount($discountPercentage%)"
        val tvTotal = findViewById<TextView>(R.id.tvTotalCheckout)
        tvTotal.text = "0"

        imageViewPlus.setOnClickListener {
            qty += 1
            tvQty.text = qty.toString()
            tvQtyCounter.text = qty.toString()
            if (price != null) {
                subtotal = price * qty
                tvSubtotal.text = subtotal.toString()
                total = subtotal - discount!!
                tvTotal.text = total.toString()
            }
        }
        imageViewMinus.setOnClickListener {
            qty -= 1
            tvQty.text = qty.toString()
            tvQtyCounter.text = qty.toString()
            if (price != null) {
                subtotal = price * qty
                tvSubtotal.text = subtotal.toString()
                total = subtotal - discount!!
                tvTotal.text = total.toString()
            }
        }

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Months are zero-based
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val currentDate = String.format("%04d-%02d-%02d", year, month, day)

        val btnBuy = findViewById<Button>(R.id.btnBuyCheckout)
        btnBuy.setOnClickListener {
            databaseReference = FirebaseDatabase.getInstance().getReference("user")
            val receivedOrder = RecievedOrder(
                userID,
                userName,
                editTextAddress.text.toString(),
                editTextEmail.text.toString(),
                editTextPhone.text.toString(),
                editTextNote.text.toString(),
                itemID,
                itemName,
                itemImage,
                qty.toString(),
                total.toString(),
                currentDate,
                "Pending"
            )
            if (itemOwner != null) {
                databaseReference.child(itemOwner).child("receivedOrders").push().setValue(receivedOrder).addOnSuccessListener {
                    Toast.makeText(this, "Order is recorded successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SelectedItemActivity::class.java)
                    intent.putExtra("itemID", itemID)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Order is failed to record", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}