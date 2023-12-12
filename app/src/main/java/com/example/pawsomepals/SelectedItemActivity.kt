package com.example.pawsomepals

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class SelectedItemActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseReferenceUser: DatabaseReference

    private lateinit var tvTitle: TextView
    private lateinit var tvHeader: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvBrand: TextView
    private lateinit var tvCondition: TextView
    private lateinit var tvColor: TextView
    private lateinit var tvWeight: TextView
    private lateinit var tvSeller: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_item)

        supportActionBar?.hide()

        //popup overlay
        val overlayView = View(this)
        overlayView.setBackgroundResource(R.drawable.popup_overlay)
        overlayView.alpha = 1f
        overlayView.isClickable = false
        val container = findViewById<FrameLayout>(R.id.popupPackageContainer)

        val packageName = this.packageName
        val preferencesName = "$packageName.user_preferences"
        val sharedPreferences = this.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        val userID = sharedPreferences.getString("userID","").toString()
        val userName = sharedPreferences.getString("userName","").toString()

        tvTitle = findViewById(R.id.tvSelectedItemTitle)
        tvHeader = findViewById(R.id.tvHeaderSelectedItem)
        tvPrice = findViewById(R.id.tvPriceSelectedItem)
        tvDescription = findViewById(R.id.tvSelectedItemDescription)
        tvBrand = findViewById(R.id.tvSelectedItemBrand)
        tvCondition = findViewById(R.id.tvSelectedItemCondition)
        tvColor = findViewById(R.id.tvSelectedItemColor)
        tvWeight = findViewById(R.id.tvSelectedItemWeight)
        tvSeller = findViewById(R.id.tvSelectedItemSeller)

        val itemID = intent.getStringExtra("itemID")

        if (itemID != null){
            databaseReference = FirebaseDatabase.getInstance().getReference("marketplaceItem")
            databaseReference.child(itemID).get().addOnSuccessListener {
                if (it.exists()){
                    val image = it.child("image").value.toString()
                    val title = it.child("title").value.toString()
                    val price = it.child("price").value.toString()
                    val discount = it.child("discount").value.toString()
                    val description = it.child("description").value.toString()
                    val condition = it.child("condition").value.toString()
                    val brand = it.child("brand").value.toString()
                    val color = it.child("color").value.toString()
                    val weight = it.child("weight").value.toString()
                    val owner = it.child("owner").value.toString()

                    tvTitle.text = title
                    tvHeader.text = title
                    tvPrice.text = price
                    tvDescription.text = description
                    tvCondition.text = condition
                    tvBrand.text = brand
                    tvColor.text = color
                    tvWeight.text = weight
                    var selectedItemImage = findViewById<ImageView>(R.id.imageSelectedItem)
                    Picasso.get().load(image).into(selectedItemImage)
                    databaseReferenceUser = FirebaseDatabase.getInstance().getReference("user")
                    databaseReferenceUser.child(owner).get().addOnSuccessListener {dataSnapshot ->
                        if (dataSnapshot.exists()){
                            val sellerName = dataSnapshot.child("name").value.toString()
                            tvSeller.text = sellerName
                        }
                    }

                    val btnSellerMoreItem = findViewById<Button>(R.id.btnSellerMoreItem)

                    btnSellerMoreItem.setOnClickListener {
                        val intent = Intent(this, MarketplaceActivity::class.java)
                        intent.putExtra("sellerID", owner)
                        startActivity(intent)
                    }

                    val btnSubmit = findViewById<Button>(R.id.btnSubmitSelectedItem)
                    val btnDelete = findViewById<Button>(R.id.btnDeleteSelectedItem)
                    if(owner == userID){
                        btnSubmit.text = "Update"
                        btnDelete.visibility = View.VISIBLE

                        btnSubmit.setOnClickListener {
                            var intent = Intent(this, UpdateItemActivity::class.java)
                            intent.putExtra("itemID", itemID)
                            startActivity(intent)
                        }

                        btnDelete.setOnClickListener {
                            val popupView = layoutInflater.inflate(R.layout.layout_popup, null)
                            val popupLinearLayout = popupView.findViewById<LinearLayout>(R.id.popupLinearLayout)

                            val tvPopup = popupView.findViewById<TextView>(R.id.tvPopup)
                            tvPopup.setText("Do you want to delete this item")

                            val btnClose = popupView.findViewById<ImageView>(R.id.closeBtn)

                            val btnYes = popupView.findViewById<Button>(R.id.btnYes)
                            val btnNo = popupView.findViewById<Button>(R.id.btnNo)


                            val popupWindow = PopupWindow(popupView,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT)
                            popupWindow.isFocusable = false

                            popupWindow.showAtLocation(container, Gravity.CENTER, 0, 0)

                            container.addView(overlayView)

                            btnNo.setOnClickListener{
                                container.removeView(overlayView)
                                popupWindow.dismiss()
                            }

                            btnYes.setOnClickListener {
                                databaseReference = FirebaseDatabase.getInstance().getReference("marketplaceItem")
                                databaseReference.child(itemID).removeValue().addOnSuccessListener {

                                    Toast.makeText(this@SelectedItemActivity, "Item Data was Deleted", Toast.LENGTH_SHORT).show()

                                    val intent = Intent(this@SelectedItemActivity, MarketplaceActivity::class.java)
                                    intent.putExtra("sellerID", userID)
                                    startActivity(intent)
                                    finish()
                                }.addOnFailureListener {

                                }
                            }
                        }
                    }else{
                        btnSubmit.text = "Order"

                        btnSubmit.setOnClickListener {
                            val intent = Intent(this, CheckoutActivity::class.java)
                            intent.putExtra("itemID", itemID)
                            intent.putExtra("itemName", title)
                            intent.putExtra("itemPrice", price)
                            intent.putExtra("itemDiscount", discount)
                            intent.putExtra("itemImage", image)
                            intent.putExtra("itemOwner", owner)
                            intent.putExtra("userName", userName)
                            intent.putExtra("userID", userID)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}