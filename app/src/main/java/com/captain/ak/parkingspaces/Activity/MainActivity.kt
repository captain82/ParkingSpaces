package com.captain.ak.parkingspaces.Activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.captain.ak.parkingspaces.Adapters.MainPagerAdapter
import com.captain.ak.parkingspaces.R
import com.captain.ak.parkingspaces.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {


    private lateinit  var mAuth:FirebaseAuth

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar!!.elevation = 0F





        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        val fragmentAdapter = MainPagerAdapter(supportFragmentManager)
        binding.mainPager.adapter = fragmentAdapter
        binding.tabsMain.setupWithViewPager(binding.mainPager)


    }

    override fun onStart() {
        super.onStart()

        val currentUser: FirebaseUser? = mAuth.currentUser

        if (currentUser==null)
        {
            sendToStart()
        }

    }

    private fun sendToStart() {

        val intent = Intent(this,LogInActivity::class.java)

        startActivity(intent)

        finish()


    }
}
