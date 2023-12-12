package com.example.pawsomepals.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.pawsomepals.R
import com.example.pawsomepals.LostAndFoundLandingActivity
import com.example.pawsomepals.FetchingVetInfo

class ServicesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_services, container, false)

        val lostAndFoundButton = view.findViewById<Button>(R.id.lostAndFoundButton)

        lostAndFoundButton.setOnClickListener {
            val intent = Intent(context, LostAndFoundLandingActivity::class.java)
            startActivity(intent)
        }

        val vetServicesButton = view.findViewById<Button>(R.id.vetServicesButton)

        vetServicesButton.setOnClickListener {
            val intent = Intent(context, FetchingVetInfo::class.java)
            startActivity(intent)
        }


        return view
    }
}