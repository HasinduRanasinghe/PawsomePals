package com.example.pawsomepals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class VetAdapter (private val vetsList: ArrayList<Vet>): RecyclerView.Adapter<VetAdapter.ViewHolder>(){

    private lateinit var clickListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setItemClickListner(listener: onItemClickListener){
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.vet_list,parent, false)
        return ViewHolder(itemView, clickListener)
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val currentVet = vetsList[position]

        holder.vetName.text = currentVet.vetName
        holder.degree.text = currentVet.degree
        holder.clinic.text = currentVet.clinic
        holder.contact.text = currentVet.contact
        // Load image using Picasso
        Picasso.get().load(currentVet.imageURL).into(holder.petImageView)
//        Picasso.get().load(currentPet.petImageView).error(R.drawable.pug).into(holder.petImageView)

    }

    override fun getItemCount(): Int {
        return vetsList.size
    }


    class ViewHolder(itemView: View, listener: onItemClickListener):RecyclerView.ViewHolder(itemView) {

        val vetName: TextView = itemView.findViewById(R.id.vetName)
        val degree: TextView = itemView.findViewById(R.id.degree)
        val clinic: TextView = itemView.findViewById(R.id.clinic)
        val contact: TextView = itemView.findViewById(R.id.contact)
        val petImageView: ImageView = itemView.findViewById(R.id.profilePicture)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }
}