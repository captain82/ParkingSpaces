package com.captain.ak.parkingspaces.viewModel

import android.arch.lifecycle.ViewModel
import android.view.View
import com.captain.ak.parkingspaces.Interface.HomeFragmentCallbacks

class HomeFragmentViewModel(var homeFragmentCallbacks: HomeFragmentCallbacks):ViewModel() {

    fun addnewBtnClicked(view: View)
    {
        homeFragmentCallbacks.addNewSpace()
    }



}