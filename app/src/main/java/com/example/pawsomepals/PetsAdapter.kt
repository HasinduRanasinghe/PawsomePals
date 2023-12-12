package com.example.pawsomepals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class PetsAdapter (private val petsList: ArrayList<PetInformationData>): RecyclerView.Adapter<PetsAdapter.ViewHolder>(){

    private lateinit var pListner: onIteamClickListner
    interface onIteamClickListner{
        fun onItemClick(posiion: Int)
    }

    fun setItemClickListner(clickListner: onIteamClickListner){
        pListner =clickListner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetsAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pets_listview,parent, false)
        return ViewHolder(itemView, pListner)
    }

    override fun onBindViewHolder(holder: PetsAdapter.ViewHolder, position: Int) {
        val currentPet = petsList[position]

        holder.petName.text = currentPet.petName
        holder.breed.text = currentPet.breed
        holder.address.text = currentPet.address
        holder.age.text = currentPet.age
        // Load image using Picasso
        Picasso.get().load(currentPet.imageURL).into(holder.petImageView)
//        Picasso.get().load(currentPet.petImageView).error(R.drawable.pug).into(holder.petImageView)

    }

    override fun getItemCount(): Int {
        return petsList.size
    }


    class ViewHolder(itemView: View, clickListner: onIteamClickListner):RecyclerView.ViewHolder(itemView) {

        val petName: TextView = itemView.findViewById(R.id.petName)
        val breed: TextView = itemView.findViewById(R.id.breed)
        val address: TextView = itemView.findViewById(R.id.address)
        val age: TextView = itemView.findViewById(R.id.age)
        val petImageView: ImageView = itemView.findViewById(R.id.petImage)

        init {
            itemView.setOnClickListener{
                clickListner.onItemClick(adapterPosition)
            }
        }
    }
}