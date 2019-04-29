package com.captain.ak.parkingspaces.viewModel

import android.arch.lifecycle.ViewModel
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.captain.ak.parkingspaces.Interface.LoginResultCallbacks
import com.captain.ak.parkingspaces.Models.User

open class LoginViewModel( var loginResultCallbacks: LoginResultCallbacks) : ViewModel() {

     var user = User()

    //Method to get Email from Edit Text and set to user
    fun getEmailTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                user.email = s.toString()

            }
        }
    }

    fun getPasswordTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                user.password = s.toString()

            }
        }
    }

    //Method to perform Login

    fun onLoginClicked(view: View) {

        var error_code: Int = user.isValidData

        if (error_code == 0) {
            loginResultCallbacks.onError("You must Enter valid Email Address")
        } else if (error_code == 1) {
            loginResultCallbacks.onError("Your Email is not valid")
        } else if (error_code == 2) {
            loginResultCallbacks.onError("Password must be greater than 6 characters")
        } else {
            loginResultCallbacks.onSuccess("Login Success")
        }
    }
}