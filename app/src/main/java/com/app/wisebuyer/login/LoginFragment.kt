package com.app.wisebuyer.login

import android.graphics.Color
import com.app.wisebuyer.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels


class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(
            R.layout.fragment_login, container, false
        )
        val usernameInput = view.findViewById<EditText>(R.id.username_input)
        val passwordInput = view.findViewById<EditText>(R.id.password_input)

        val button:Button = view.findViewById<Button>(R.id.login_button)
        button.setBackgroundColor(Color.parseColor("#00FF00"))

        button.setOnClickListener {
            Log.v("APP", "Login button clicked")
            val credentials = UserCredentials(usernameInput.text.toString(), passwordInput.text.toString())
            loginViewModel.loginUser(credentials)
        }
        // Handle the result here
        // For example, update UI based on the login result
        // result is the value emitted by the LiveData
        loginViewModel.loginResult.observe(viewLifecycleOwner) { result: Boolean ->
            button.setBackgroundColor(Color.parseColor("#FF0000"))
            Log.v("APP", "login result: $result")
        }
        return view
    }
}