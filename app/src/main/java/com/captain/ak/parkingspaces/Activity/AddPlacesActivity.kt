package com.captain.ak.parkingspaces.Activity

import android.annotation.SuppressLint
import android.content.Context
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
import java.util.*
import kotlin.collections.HashMap


class AddPlacesActivity : AppCompatActivity() {

    lateinit var binding:ActivityAddPlacesBinding

    lateinit var mAuth:FirebaseAuth

    lateinit var databaseRef:DatabaseReference

    lateinit var locationManager:LocationManager

    private var hasGps = false

    private var hasNetwork = false

    private  var locationGps:Location? = null

    private  var locationNetwork:Location? = null

     var count:String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, com.captain.ak.parkingspaces.R.layout.activity_add_places)

        mAuth = FirebaseAuth.getInstance()

        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users")

        binding.uploadBtn.setOnClickListener { upload() }
    }


    override fun onResume() {
        super.onResume()


    }

    @SuppressLint("MissingPermission")
    private fun upload() {
        val currentUID = mAuth.currentUser!!.uid
//********************For getting Lattitiude and Longitude***************************************************************
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if(hasGps||hasNetwork) {
            if (hasGps) {
                Log.d("CodeAndroidLocation", "hasGps")
                locationManager
                    .requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F, object : LocationListener {
                        override fun onLocationChanged(location: Location?) {
                            if (location != null) {
                                locationGps = location
                                binding.lattitudeTxt.setText("Lattitude : "+locationGps!!.latitude.toString())
                                binding.longitudeTxt.setText("Longitude : "+locationGps!!.longitude.toString())

                                Log.d("CodeAndroidLocation1" , "Network lattitude: "+locationGps.toString())
                                Log.d("CodeAndroidLocation1" , "Network longitude: "+locationGps!!.longitude)
                            }
                        }

                        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                        }

                        override fun onProviderEnabled(provider: String?) {
                        }

                        override fun onProviderDisabled(provider: String?) {
                        }


                    })

                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null) {
                    locationGps = localGpsLocation
                    Log.d("CodeAndroidLocation1" , "Network lattitude: "+locationGps!!.longitude.toString())

                }
            }

            if (hasNetwork) {
                Log.d("CodeAndroidLocation", "hasNetwork")
                locationManager
                    .requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0F, object : LocationListener {
                        override fun onLocationChanged(location: Location?) {
                            if (location != null) {
                                locationNetwork = location
                                binding.lattitudeTxt.setText("Lattitude : "+locationNetwork!!.latitude.toString())
                                binding.longitudeTxt.setText("Longitude : "+locationNetwork!!.longitude.toString())
                                Log.i("Location" , "Network lattitude: "+locationNetwork!!.latitude.toString())
                                Log.i("Location" , "Network longitude: "+locationNetwork!!.longitude.toString())

                            }
                        }

                        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                        }

                        override fun onProviderEnabled(provider: String?) {
                        }

                        override fun onProviderDisabled(provider: String?) {
                        }


                    })

                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (localNetworkLocation != null) {
                    locationNetwork = localNetworkLocation
                }
            }
            if(locationGps!=null && locationNetwork!=null)
            {
                if(locationGps!!.accuracy>locationNetwork!!.accuracy)
                {
                    binding.lattitudeTxt.setText("Lattitude : "+locationNetwork!!.latitude.toString())
                    binding.longitudeTxt.setText("Longitude : "+locationNetwork!!.longitude.toString())
                    Log.d("CodeAndroidLocation" , "Network lattitude: "+locationNetwork!!.latitude)
                    Log.d("CodeAndroidLocation" , "Network longitude: "+locationNetwork!!.longitude)

                }
                else
                {
                    binding.lattitudeTxt.setText("Lattitude : "+locationGps!!.latitude.toString())
                    binding.longitudeTxt.setText("Longitude : "+locationGps!!.longitude.toString())

                    Log.d("CodeAndroidLocation" , "Network lattitude: "+locationGps!!.latitude)
                    Log.d("CodeAndroidLocation" , "Network longitude: "+locationGps!!.longitude)

                }

            }
        }
//*************************************End of getting Lattitude and Longitude*******************************8888888


        //********************For getting street address***********************************************************
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(this, Locale.getDefault())


        addresses = geocoder.getFromLocation(
            locationGps!!.latitude,
            locationGps!!.longitude,
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

//*****************************************End of getting street Address****************************************************************8

//***********************************For getting count**************************************************************


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

        userMap["lattitude"] = locationGps!!.latitude.toString()

        userMap["longitude"] = locationGps!!.longitude.toString()

        userMap["address"] = binding.streetEditText.text.toString()

        userMap["description"] = binding.descEditText.text.toString()

        userMap["slots"] = binding.slotsEditText.text.toString()

        database.setValue(userMap)

        //*************for increasing the count**************

        databaseRef.child(currentUID).child("count").setValue((count.toInt()+1).toString())

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

