package com.example.pawsomepals.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawsomepals.DisplayOrderActivity
import com.example.pawsomepals.R
import com.example.pawsomepals.SelectedItemActivity
import com.example.pawsomepals.dataclass.RecievedOrder
import com.squareup.picasso.Picasso

class ReceivedOrderAdapter(
    private val marketplaceItems: List<RecievedOrder>,
    private val itemIDs: List<String>,
    private val context: Context,
): RecyclerView.Adapter<ReceivedOrderAdapter.ReceivedOrderViewHolder>() {
    class ReceivedOrderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvViewOrdersItemName: TextView = itemView.findViewById(R.id.tvViewOrdersItemName)
        val tvViewOrdersOrderedDate: TextView = itemView.findViewById(R.id.tvViewOrdersOrderedDate)
        val imageViewOrderedItem: ImageView = itemView.findViewById((R.id.imageViewOrderedItem))
        val imageViewNavToMore: ImageView = itemView.findViewById((R.id.imageViewNavToMore))
        val tvViewOrdersItemQty: TextView = itemView.findViewById(R.id.tvViewOrdersItemQty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivedOrderViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_received_order, parent, false)
        return ReceivedOrderViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ReceivedOrderViewHolder, position: Int) {
        val orderItem = marketplaceItems[position]
        val itemID = itemIDs[position]
        holder.tvViewOrdersItemName.text = orderItem.itemName
        holder.tvViewOrdersOrderedDate.text = orderItem.date
        Picasso.get().load(orderItem.itemImage).into(holder.imageViewOrderedItem)
        holder.tvViewOrdersItemQty.text = "Qty: ${orderItem.qty}"

        holder.imageViewNavToMore.setOnClickListener {
            val intent = Intent(context, DisplayOrderActivity::class.java)
            intent.putExtra("itemID", itemID)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = marketplaceItems.size
}