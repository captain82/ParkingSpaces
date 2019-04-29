package com.captain.ak.parkingspaces.Fragments


import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.captain.ak.parkingspaces.Activity.AddPlacesActivity
import com.captain.ak.parkingspaces.Activity.MainActivity
import com.captain.ak.parkingspaces.Interface.HomeFragmentCallbacks

import com.captain.ak.parkingspaces.R
import com.captain.ak.parkingspaces.databinding.FragmentHomeBinding
import com.captain.ak.parkingspaces.viewModel.HomeFragmentViewModel
import com.captain.ak.parkingspaces.viewModel.HomeFragmentViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment(),HomeFragmentCallbacks {

    lateinit var binding:FragmentHomeBinding

    lateinit var mAuth:FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)

        val myroot:View = binding.root

        binding.viewModel = ViewModelProviders
            .of(this,HomeFragmentViewModelFactory(this))
            .get(HomeFragmentViewModel::class.java)

        return myroot

        }

    override fun addNewSpace() {
        val intent = Intent(context,AddPlacesActivity::class.java)
        startActivity(intent)
    }

    override fun logout() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(context,MainActivity::class.java)
        startActivity(intent)
        onDestroy()
    }




}
