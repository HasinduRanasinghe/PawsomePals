package com.example.pawsomepals

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class LostAndFoundLostAdapter(private val lostPets: List<LostPet>, private val context: Context) : RecyclerView.Adapter<LostAndFoundLostAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_lostPage)
        val petNameTextView: TextView = itemView.findViewById(R.id.tv_lostPageRIPetName)
        val genderTextView: TextView = itemView.findViewById(R.id.tv_lostPageRIPetMF)
        val breedTextView: TextView = itemView.findViewById(R.id.tv_lostPageRIPetBreed)
        val locationTextView: TextView = itemView.findViewById(R.id.tv_lostPageRIPetLSLoc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lost_list_items, parent, false)
        return ViewHolder(view)
    }

    interface OnItemClickListener {
        fun onItemClick(lostPet: LostPet)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pet = lostPets[position]

        holder.petNameTextView.text = pet.petName
        holder.genderTextView.text = pet.gender
        holder.breedTextView.text = pet.breed
        holder.locationTextView.text = pet.location

        // Load the image from the specified URL using Picasso or Glide
        Picasso.get().load(pet.imageURL).into(holder.imageView)

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(pet)
        }
    }

    override fun getItemCount() = lostPets.size
}
