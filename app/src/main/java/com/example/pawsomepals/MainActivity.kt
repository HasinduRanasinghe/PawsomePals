package com.example.pawsomepals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.pawsomepals.databinding.ActivityMainBinding
import com.example.pawsomepals.fragments.HomeFragment
import com.example.pawsomepals.fragments.MarketplaceFragment
import com.example.pawsomepals.fragments.MenuFragment
import com.example.pawsomepals.fragments.ServicesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        loadFragment(HomeFragment())

        var resHotelBottomNav = findViewById<BottomNavigationView>(R.id.resHotelBottomNav)
        resHotelBottomNav.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.navItemHome -> {
                    loadFragment(HomeFragment())
                    true
                }R.id.navItemMarketplace -> {
                    loadFragment(MarketplaceFragment())
                    true
                }R.id.navItemServices -> {
                    loadFragment(ServicesFragment())
                    true
                }R.id.navItemMenu -> {
                    loadFragment(MenuFragment())
                    true
                }else -> false
            }
        }
    }
    private fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment).commit()
    }
}