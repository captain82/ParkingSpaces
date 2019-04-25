package com.captain.ak.parkingspaces.Activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.captain.ak.parkingspaces.R
import com.captain.ak.parkingspaces.databinding.ActivitySignupBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.HashMap

class SignupActivity : AppCompatActivity() {


    lateinit var binding: ActivitySignupBinding

    lateinit var mAuth: FirebaseAuth

    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)

        mAuth = FirebaseAuth.getInstance()

        binding.signupBtn.setOnClickListener { register() }


    }

    private fun register() {

        var name: String = binding.signupName.text.toString()

        var email: String = binding.signupEmailEditText.text.toString()

        var password: String = binding.signupPassEditText.text.toString()

        if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
            registerUser(email, password, name)
        }


    }

    private fun registerUser(email: String, password: String, name: String) {

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
                    val uid: String = user.uid
                    database = FirebaseDatabase.getInstance().reference.child("Users").child(uid)

                    val userMap = HashMap<String, String>()

                    userMap["name"] = name

                    userMap["count"] = "0"

                    database.setValue(userMap).addOnCompleteListener{task->
                    if (task.isComplete) {
                        val mainIntent = Intent(this, MainActivity::class.java)
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(mainIntent)
                        finish()
                    }}

                }
                else
                        {
                            Toast.makeText(
                                this, "Authentication failed.",
                                Toast.LENGTH_LONG
                            ).show()

                        }
            }

    }


}
