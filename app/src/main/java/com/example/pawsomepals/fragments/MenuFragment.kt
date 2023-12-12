package com.example.pawsomepals.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.pawsomepals.LoginActivity
import com.example.pawsomepals.MainActivity
import com.example.pawsomepals.R
import com.example.pawsomepals.RegisterActivity

class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        val parentLayout = view.findViewById<RelativeLayout>(R.id.dinamicProfileContainer)

        val dynamicLayout = RelativeLayout(this.context)
        dynamicLayout.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )

        val packageName = requireContext().packageName
        val preferencesName = "$packageName.user_preferences"
        val sharedPreferences = requireContext().getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("userEmail","").toString()

        if(userEmail.isNullOrEmpty() && userEmail.isNullOrBlank()){
            val buttonLogin = Button(requireContext())
            buttonLogin.text = "Sign In"
            buttonLogin.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            buttonLogin.setBackgroundResource((R.drawable.round_corner_button))
            val buttonLoginParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            buttonLoginParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            buttonLogin.layoutParams = buttonLoginParams
            dynamicLayout.addView(buttonLogin)

            buttonLogin.setOnClickListener{
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }

            val buttonRegister = Button(requireContext())
            buttonRegister.text = "Sign Up"
            buttonRegister.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            buttonRegister.setBackgroundResource((R.drawable.round_corner_button))
            val buttonRegisterParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            buttonRegisterParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            buttonRegister.layoutParams = buttonRegisterParams
            dynamicLayout.addView(buttonRegister)

            buttonRegister.setOnClickListener{
                val intent = Intent(requireContext(), RegisterActivity::class.java)
                startActivity(intent)
            }
        }else{
            val buttonLogout = Button(requireContext())
            buttonLogout.text = "Sign Up"
            buttonLogout.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            buttonLogout.setBackgroundResource((R.drawable.round_corner_button))
            val buttonLogoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            buttonLogoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            buttonLogout.layoutParams = buttonLogoutParams
            dynamicLayout.addView(buttonLogout)

            buttonLogout.setOnClickListener {
                val packageName = requireContext().packageName
                val preferencesName = "$packageName.user_preferences"
                val sharedPreferences = requireContext().getSharedPreferences(preferencesName, Context.MODE_PRIVATE)

                val editor = sharedPreferences.edit()

                editor.remove("userEmail")
                editor.remove("userID")

                editor.apply()

                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
            }
        }
        parentLayout.addView(dynamicLayout)
        return  view
    }
}