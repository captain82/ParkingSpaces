package com.captain.ak.parkingspaces.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.captain.ak.parkingspaces.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {


     lateinit var mAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //FirebaseApp.initializeApp(this)


        mAuth = FirebaseAuth.getInstance()

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


    }
}
