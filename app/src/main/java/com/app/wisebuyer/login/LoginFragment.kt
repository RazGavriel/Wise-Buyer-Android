package com.app.wisebuyer.login

import com.app.wisebuyer.R
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by activityViewModels()
    private lateinit var emailInput : EditText
    private lateinit var passwordInput : EditText
    private lateinit var loginButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(
            R.layout.fragment_login, container, false
        )
        emailInput = view.findViewById<EditText>(R.id.email_input)
        passwordInput = view.findViewById<EditText>(R.id.password_input)
        loginButton = view.findViewById<Button>(R.id.login_button)

        handleButtonClick(loginButton)
        // Handle the result here
        observeLoginResult()
        return view
    }

    private fun observeLoginResult() {
        loginViewModel.loginResult.observe(viewLifecycleOwner) { result: Boolean ->
            Log.v("APP", "login result: $result")
            if (result) {
                findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
            }
        }
    }

    private fun handleButtonClick(loginButton:Button) {
        loginButton.setOnClickListener {
            Log.v("APP", "Login button clicked")
            val credentials =
                UserCredentials(emailInput.text.toString(), passwordInput.text.toString())
            if (checkAllFields(credentials)) {
                loginViewModel.loginUser(credentials)
            }
        }
    }

    private fun checkAllFields(credentials: UserCredentials): Boolean{
        if (credentials.email.isEmpty()){
            emailInput.error = "Enter a email"
        }
        else if (credentials.password.isEmpty()){
            passwordInput.error = "Enter a password"
        }
        else if (credentials.password.length <=6) {
            passwordInput.error = "Password need to more than 6 characters long"
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(credentials.email).matches()){
            emailInput.error = "Enter valid email format"
        }
        else{ return true }
        return false
    }
}