package com.captain.ak.parkingspaces.Activity

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.captain.ak.parkingspaces.Models.ParkingModel
import com.captain.ak.parkingspaces.Models.UserModel
import com.captain.ak.parkingspaces.R
import com.captain.ak.parkingspaces.databinding.ActivityAddPlacesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AddPlacesActivity : AppCompatActivity() {

    lateinit var binding:ActivityAddPlacesBinding

    lateinit var mAuth:FirebaseAuth

    lateinit var databaseRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_places)

        mAuth = FirebaseAuth.getInstance()

        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users")

        binding.uploadBtn.setOnClickListener { upload() }
    }

    private fun upload() {
        val currentUID = mAuth.currentUser!!.uid

        databaseRef.child(currentUID).child("saved_parking").addValueEventListener(object :ValueEventListener
        {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(p0: DataSnapshot?) {

                var items:Iterator<DataSnapshot> = p0!!.children.iterator()


                while(items.hasNext()) {
                    val item:DataSnapshot = items.next()
                    val parkingModel = ParkingModel()
                    parkingModel.lattitude = item.getValue(ParkingModel::class.java)!!.lattitude
                    parkingModel.longitude = item.getValue(ParkingModel::class.java)!!.longitude

                    Log.i("DAta", parkingModel.lattitude)


                }




            }
        })



    }








       /* databaseRef.child(currentUID).child("No of places").setValue(1)
            .addOnSuccessListener{
            }*/


    }

