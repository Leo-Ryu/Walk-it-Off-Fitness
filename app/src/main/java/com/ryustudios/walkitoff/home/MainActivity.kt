package com.ryustudios.walkitoff.home

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ryustudios.walkitoff.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var currNavigationItem = R.id.navigation_home

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                if (currNavigationItem != R.id.navigation_home) {
                    val homeFragment = HomeFragment.newInstance()
                    openFragment(homeFragment)
                    currNavigationItem = R.id.navigation_home
                    return@OnNavigationItemSelectedListener true
                }

            }
            R.id.navigation_dashboard -> {
                currNavigationItem = R.id.navigation_dashboard
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                currNavigationItem = R.id.navigation_notifications
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        openFragment(HomeFragment.newInstance())
    }



}
