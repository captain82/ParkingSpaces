package com.captain.ak.parkingspaces.viewModel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.captain.ak.parkingspaces.Interface.LoginResultCallbacks

open class LoginViewModelFactory(val loginResultCallbacks: LoginResultCallbacks) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(loginResultCallbacks) as T
    }
}
