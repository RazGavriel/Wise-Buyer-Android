package com.app.wisebuyer.singup

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.wisebuyer.R
import com.app.wisebuyer.login.LoginViewModel
import com.app.wisebuyer.login.UserCredentials

class SignupFragment: Fragment() {

    private val signUpViewModel: SignUpViewModel by activityViewModels()
    private lateinit var emailInput : EditText
    private lateinit var passwordInput : EditText
    private lateinit var signUpButton: Button
    private lateinit var messageBox : TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(
            R.layout.fragment_signup, container, false
        )
        emailInput = view.findViewById<EditText>(R.id.email_input)
        passwordInput = view.findViewById<EditText>(R.id.password_input)
        signUpButton = view.findViewById<Button>(R.id.sign_up_button)
        messageBox = view.findViewById<TextView>(R.id.message_box)

        handleSignUpClick(signUpButton)
        observeSignUpResult()
        return view
    }

    override fun onResume() {
        Log.v("APP", "signup resume")
        resetParameters()
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        signUpViewModel.clearSignUpResult()
    }

    private fun resetParameters(){
        emailInput.text.clear()
        passwordInput.text.clear()
        messageBox.text = ""
    }

    @SuppressLint("SetTextI18n")
    private fun observeSignUpResult() {
        signUpViewModel.signUpResult.observe(viewLifecycleOwner) { result: Boolean ->
            Log.v("APP", "signUp result: $result")
            if (result) {
                findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
            }
            else {
                messageBox.visibility = View.VISIBLE
                messageBox.text = "The email is already in use"
            }
        }
    }
    private fun handleSignUpClick(signUpButton:Button) {
        signUpButton.setOnClickListener{
            messageBox.visibility = View.INVISIBLE
            Log.v("APP", "signUp button clicked")
            val credentials = UserCredentials(emailInput.text.toString(), passwordInput.text.toString())
            if (checkAllFields(credentials)) {
                signUpViewModel.signUpUser(credentials)
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