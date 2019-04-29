package com.captain.ak.parkingspaces.viewModel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.captain.ak.parkingspaces.Interface.HomeFragmentCallbacks

class HomeFragmentViewModelFactory(val homeFragmentCallbacks: HomeFragmentCallbacks):
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeFragmentViewModel(homeFragmentCallbacks) as T
    }
}