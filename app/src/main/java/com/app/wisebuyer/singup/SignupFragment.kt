package com.app.wisebuyer.singup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.wisebuyer.MainActivity
import com.app.wisebuyer.R
import com.app.wisebuyer.login.UserCredentials
import com.app.wisebuyer.login.UserMetaData
import com.app.wisebuyer.utils.checkCredentials
import com.app.wisebuyer.utils.checkMetaData

class SignupFragment: Fragment() {

    private val signUpViewModel: SignUpViewModel by activityViewModels()
    private lateinit var emailInput : EditText
    private lateinit var passwordInput : EditText
    private lateinit var firstNameInput : EditText
    private lateinit var lastNameInput : EditText
    private lateinit var signUpButton: Button
    private lateinit var messageBox : TextView
    private lateinit var progressBarSignUp: ProgressBar

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
        progressBarSignUp = view.findViewById<ProgressBar>(R.id.progress_bar_sign_up)

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
        firstNameInput.text.clear()
        lastNameInput.text.clear()
        messageBox.text = ""
    }

    private fun observeSignUpResult() {
        signUpViewModel.signUpResult.observe(viewLifecycleOwner) { result: String ->
            if (result == "Success") {
                findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
            }
            else {
                messageBox.visibility = View.VISIBLE
                messageBox.text = result
            }
            progressBarSignUp.visibility = View.GONE
        }
    }
    private fun handleSignUpClick(signUpButton:Button) {
        signUpButton.setOnClickListener{
            messageBox.visibility = View.INVISIBLE
            val credentials = UserCredentials(emailInput.text.toString(), passwordInput.text.toString())
            val userMetaData = UserMetaData(firstNameInput.text.toString(), lastNameInput.text.toString())
            if (checkCredentials(credentials, emailInput, passwordInput) &&
                checkMetaData(userMetaData, firstNameInput, lastNameInput))
            {
                progressBarSignUp.visibility = View.VISIBLE
                signUpViewModel.signUpUser(credentials, userMetaData)
            }
        }
    }

}