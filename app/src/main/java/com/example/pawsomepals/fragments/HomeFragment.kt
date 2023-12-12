package com.example.pawsomepals.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.pawsomepals.R
import com.example.pawsomepals.FetchingPetsInfor

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val adoptionButton = view.findViewById<Button>(R.id.Button)

        adoptionButton.setOnClickListener {
            val intent = Intent(context, FetchingPetsInfor::class.java)

            startActivity(intent)
        }

        return view
    }
}