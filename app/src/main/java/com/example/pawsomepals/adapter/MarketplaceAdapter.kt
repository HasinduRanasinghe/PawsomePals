package com.example.pawsomepals.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pawsomepals.LoginActivity
import com.example.pawsomepals.R
import com.example.pawsomepals.SelectedItemActivity
import com.example.pawsomepals.dataclass.MarketplaceItem
import com.squareup.picasso.Picasso

class MarketplaceAdapter(
    private val marketplaceItems: List<MarketplaceItem>,
    private val itemIDs: List<String>,
    private val context: Context,
    private val isOwner: List<Boolean>):
    RecyclerView.Adapter<MarketplaceAdapter.MarketplaceViewHolder>() {
    class MarketplaceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvPrice: TextView = itemView.findViewById(R.id.tvMarketplaceListPrice)
        val tvName: TextView = itemView.findViewById(R.id.tvMarketplaceListName)
        val ivMarketplaceItem: ImageView = itemView.findViewById((R.id.imageViewMarketplaceItem))
        val tvBuy: TextView = itemView.findViewById(R.id.tvMarketplaceListBuy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketplaceViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_marketplace, parent, false)
        return MarketplaceViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MarketplaceViewHolder, position: Int) {
        val marketplaceItem = marketplaceItems[position]
        val itemID = itemIDs[position]
        holder.tvPrice.text = "Rs. ${marketplaceItem.price}"
        holder.tvName.text = marketplaceItem.title
        Picasso.get().load(marketplaceItem.image).into(holder.ivMarketplaceItem)
        if (isOwner[position]){
            holder.tvBuy.text = "More"
        }
        holder.tvBuy.setOnClickListener {
            val intent = Intent(context, SelectedItemActivity::class.java)
            intent.putExtra("itemID", itemID)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = marketplaceItems.size
}