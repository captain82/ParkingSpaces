package com.captain.ak.parkingspaces.Fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.captain.ak.parkingspaces.Activity.UpdateDetails
import com.captain.ak.parkingspaces.Models.ParkingModel

import com.captain.ak.parkingspaces.R
import com.captain.ak.parkingspaces.ViewHolder.ParkingViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.parking_layout.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SavedFragment : Fragment() {

    lateinit var recyclerView: RecyclerView

    lateinit var databaseRef:DatabaseReference

    lateinit var adapter:FirebaseRecyclerAdapter<ParkingModel,ParkingViewHolder>

    lateinit var options: FirebaseRecyclerOptions<ParkingModel>

    lateinit var mAuth:FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view:View = inflater.inflate(R.layout.fragment_saved, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)

        mAuth = FirebaseAuth.getInstance()

        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.currentUser!!.uid)
            .child("saved_parking")

        options = FirebaseRecyclerOptions.Builder<ParkingModel>()
            .setQuery(databaseRef,ParkingModel::class.java).build()

        adapter =  object: FirebaseRecyclerAdapter<ParkingModel, ParkingViewHolder>(options)
        {
            override fun onBindViewHolder(holder: ParkingViewHolder, position: Int, model: ParkingModel) {
                Log.i("position" , position.toString())
                holder!!.desc.setText(model!!.description)
                holder.add.setText(model.address)
                holder.latt.setText(model.lattitude)
                holder.long.setText(model.longitude)
                holder.slot.setText(model.slots)

                holder.cardView.setOnClickListener{ val intent = Intent(context,UpdateDetails::class.java)
                intent.putExtra("position" , (position+1).toString())
                    intent.putExtra("desc" , model.description)
                    intent.putExtra("add" , model.address)
                    intent.putExtra("slot" , model.slots)
                    startActivity(intent)

                }
            }


            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ParkingViewHolder {
                return ParkingViewHolder(LayoutInflater.from(p0.context)
                    .inflate(R.layout.parking_layout,p0,false))
            }


        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.startListening()
        recyclerView.adapter=adapter


        return view
    }

    override fun onStart() {
        super.onStart()

        if (adapter!=null)
        {
            adapter.startListening()
        }
    }

    override fun onStop() {
        if (adapter!=null)
        {
            adapter.stopListening()
        }
        super.onStop()
    }

    override fun onResume() {
        super.onResume()

        if (adapter!=null)
        {
            adapter.startListening()
        }

    }


}
