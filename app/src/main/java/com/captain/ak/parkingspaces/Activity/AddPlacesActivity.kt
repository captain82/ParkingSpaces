package com.captain.ak.parkingspaces.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
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
import android.location.*
import android.location.LocationListener
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.google.android.gms.location.*
import java.lang.ClassCastException
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.HashMap


class AddPlacesActivity : AppCompatActivity() {

    lateinit var binding:ActivityAddPlacesBinding

    lateinit var mAuth:FirebaseAuth

    lateinit var databaseRef:DatabaseReference



    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    lateinit var loacationRequest: LocationRequest

    lateinit var locationCallback: LocationCallback

    lateinit var location: Location

      var count:String = "0"

    val REQUEST_CODE = 1000

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode)
        {
            REQUEST_CODE->{
                if (grantResults.size > 0)
                {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        Toast.makeText(this@AddPlacesActivity,"Permission Granted" , Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(this@AddPlacesActivity,"Permission Denied" , Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, com.captain.ak.parkingspaces.R.layout.activity_add_places)

        mAuth = FirebaseAuth.getInstance()

        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users")

        //************check Permission*************************

        if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission
                .ACCESS_FINE_LOCATION))
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION
            ),REQUEST_CODE)
        else
        {
            buildLocationRequest()
            buildLocationCallback()

            //Create FusedProviderClient
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

            if(ActivityCompat.checkSelfPermission(this@AddPlacesActivity,android.Manifest.permission
                    .ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED   &&   ActivityCompat
                    .checkSelfPermission(this@AddPlacesActivity,android.Manifest.permission
                    .ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED )
            {
                ActivityCompat.requestPermissions(this@AddPlacesActivity, arrayOf(android.Manifest.permission
                    .ACCESS_FINE_LOCATION),REQUEST_CODE)
            }
            fusedLocationProviderClient.requestLocationUpdates(loacationRequest,locationCallback, Looper.myLooper())
        }

        val currentUID = mAuth.currentUser!!.uid

        databaseRef.child(currentUID).addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(p0: DataSnapshot?) {
                val userModel = UserModel()
                userModel.count = p0!!.getValue(UserModel::class.java)!!.count
                Log.i("Count" , userModel.count.toString())
                count = userModel.count
            }


        })

        binding.uploadBtn.setOnClickListener {
            upload()
        }
    }

    private fun buildLocationCallback() {

        locationCallback = object : LocationCallback(){

            override fun onLocationResult(p0: LocationResult?) {
                 location = p0!!.locations.get(p0!!.locations.size-1) //Get last Location
                Log.i("lattitude" , location.latitude.toString()+"  "+location.longitude.toString())
                binding.longitudeTxt.setText("Longitude : "+location.longitude.toString())
                binding.lattitudeTxt.setText("Latitude : "+location.latitude.toString())

                //********************For getting street address***********************************************************
                val geocoder: Geocoder
                val addresses: List<Address>
                geocoder = Geocoder(this@AddPlacesActivity, Locale.getDefault())


                addresses = geocoder.getFromLocation(
                   location.latitude,
                    location.longitude,
                    1
                ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                val address =
                    addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                val city = addresses[0].getLocality()
                val state = addresses[0].getAdminArea()
                val country = addresses[0].getCountryName()
                val postalCode = addresses[0].getPostalCode()
                val knownName = addresses[0].getFeatureName()

                Log.i("Add" , address+city+state+country+postalCode+knownName)
                binding.streetEditText.setText(address+" "+city+" "+state)
            }


        }
    }

    @SuppressLint("RestrictedApi")
    private fun buildLocationRequest() {
        loacationRequest = LocationRequest()
        loacationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        loacationRequest.interval = 5000
        loacationRequest.fastestInterval=3000
        loacationRequest.smallestDisplacement =     10f
    }


    override fun onResume() {
        super.onResume()


    }

    private fun upload() {
        val currentUID = mAuth.currentUser!!.uid

        databaseRef.child(currentUID).addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(p0: DataSnapshot?) {
                val userModel = UserModel()
                userModel.count = p0!!.getValue(UserModel::class.java)!!.count
                Log.i("Count" , userModel.count.toString())
                count = userModel.count
            }


        })

        //*****for adding parking space*********************************************************
        var database:DatabaseReference = databaseRef.child(currentUID).child("saved_parking").child((count.toInt()+1).toString())

        var userMap = HashMap<String, String>()

        userMap["lattitude"] = location.latitude.toString()

        userMap["longitude"] = location.longitude.toString()

        userMap["address"] = binding.streetEditText.text.toString()

        userMap["description"] = binding.descEditText.text.toString()

        userMap["slots"] = binding.slotsEditText.text.toString()

        database.setValue(userMap)

        //*************for increasing the count**************

        databaseRef.child(currentUID).child("count").setValue((count.toInt()+1).toString())

        Toast.makeText(this@AddPlacesActivity,"Place updated.",Toast.LENGTH_SHORT).show()

        onBackPressed()


//***********************************************************************************






        /*

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
        })*/



    }








       /* databaseRef.child(currentUID).child("No of places").setValue(1)
            .addOnSuccessListener{
            }*/


    }

