package com.captain.ak.parkingspaces.Activity

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.captain.ak.parkingspaces.Interface.LoginResultCallbacks
import com.captain.ak.parkingspaces.R
import com.captain.ak.parkingspaces.viewModel.LoginViewModel
import com.captain.ak.parkingspaces.viewModel.LoginViewModelFactory
import com.captain.ak.parkingspaces.databinding.ActivityLogInBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId

class LogInActivity : AppCompatActivity(),LoginResultCallbacks {
    override fun onSuccess(message: String) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    lateinit var binding: ActivityLogInBinding

    lateinit var mAuth: FirebaseAuth

    lateinit var userdatabase: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar!!.elevation = 0F

        supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in)

        binding.setViewModel(
            ViewModelProviders.of(this, LoginViewModelFactory(this))
                .get(LoginViewModel::class.java)
        )






        mAuth = FirebaseAuth.getInstance()

        userdatabase = FirebaseDatabase.getInstance().getReference().child("Users")
    }

    override fun onResume() {
        super.onResume()

        binding.loginSignup.setOnClickListener { sendToRegister() }

       // binding.loginBtn.setOnClickListener { login() }
    }

    private fun login() {

        var email: String = binding.emailEditText.text.toString()

        var password: String = binding.passEditText.text.toString()

        if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
            loginUser(email, password)
        }


    }

    private fun loginUser(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener { task ->
                if (task.isSuccessful) {

                    val currentUserId: String = mAuth.currentUser!!.uid

                    val deviceToken = FirebaseInstanceId.getInstance().token

                    userdatabase.child(currentUserId).child("deviceToken").setValue(deviceToken)
                        .addOnSuccessListener(
                            OnSuccessListener<Void> {
                                Toast.makeText(this, "Successfully logged in", Toast.LENGTH_SHORT).show()

                                val mainIntent = Intent(this@LogInActivity, MainActivity::class.java)

                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                                startActivity(mainIntent)

                                finish()
                            })

                } else {
                    Toast.makeText(this, "Log in failed", Toast.LENGTH_SHORT).show()

                }
            })

    }

    private fun sendToRegister() {
        val intent = Intent(this, SignupActivity::class.java)

        startActivity(intent)


    }
}
