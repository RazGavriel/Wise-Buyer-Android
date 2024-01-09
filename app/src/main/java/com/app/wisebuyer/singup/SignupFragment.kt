package com.app.wisebuyer.singup

import android.os.Bundle
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
import com.app.wisebuyer.login.UserCredentials
import com.app.wisebuyer.login.UserMetaData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupFragment: Fragment() {

    private val signUpViewModel: SignUpViewModel by activityViewModels()
    private lateinit var emailInput : EditText
    private lateinit var passwordInput : EditText
    private lateinit var firstNameInput : EditText
    private lateinit var lastNameInput : EditText
    private lateinit var signUpButton: Button
    private lateinit var messageBox : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(
            R.layout.fragment_signup, container, false
        )
        emailInput = view.findViewById<EditText>(R.id.email_input)
        passwordInput = view.findViewById<EditText>(R.id.password_input)
        firstNameInput = view.findViewById<EditText>(R.id.first_name_input)
        lastNameInput = view.findViewById<EditText>(R.id.last_name_input)
        signUpButton = view.findViewById<Button>(R.id.sign_up_button)
        messageBox = view.findViewById<TextView>(R.id.message_box)


        handleSignUpClick(signUpButton)
        observeSignUpResult()
        return view
    }

    override fun onResume() {
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

    private fun observeSignUpResult() {
        signUpViewModel.signUpResult.observe(viewLifecycleOwner) { result: Boolean ->
            if (result) {
                findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
            }
            else {
                messageBox.visibility = View.VISIBLE
                messageBox.text = getString(R.string.EmailInUseString)
            }
        }
    }
    private fun handleSignUpClick(signUpButton:Button) {
        signUpButton.setOnClickListener{
            messageBox.visibility = View.INVISIBLE
            val credentials = UserCredentials(emailInput.text.toString(), passwordInput.text.toString())
            val userMetaData = UserMetaData(firstNameInput.text.toString(), lastNameInput.text.toString())
            if (checkCredentials(credentials) && checkMetaData(userMetaData) ) {
                signUpViewModel.signUpUser(credentials, userMetaData)
            }
        }
    }

    private fun checkMetaData(userMetaData: UserMetaData): Boolean {
        if (userMetaData.firstName.isEmpty() || !isString(userMetaData.firstName)){
            firstNameInput.error = "Enter valid first name"
        }
        else if (userMetaData.lastName.isEmpty() || !isString(userMetaData.lastName)){
            lastNameInput.error = "Enter valid last name"
        }
        else{ return true }
        return false

    }

    private fun checkCredentials(credentials: UserCredentials) : Boolean{
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

    private fun isString(value: String): Boolean {
        return value.all { it.isLetter() }
    }
}