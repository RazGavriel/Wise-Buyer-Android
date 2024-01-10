package com.app.wisebuyer.login

import com.app.wisebuyer.R
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
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by activityViewModels()
    private lateinit var emailInput : EditText
    private lateinit var passwordInput : EditText
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button
    private lateinit var messageBox : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(
            R.layout.fragment_login, container, false
        )
        emailInput = view.findViewById<EditText>(R.id.email_input)
        passwordInput = view.findViewById<EditText>(R.id.password_input)
        loginButton = view.findViewById<Button>(R.id.login_button)
        signupButton = view.findViewById<Button>(R.id.sign_up_button)
        messageBox = view.findViewById<TextView>(R.id.message_box)

        handleLoginClick(loginButton)
        handleSignUpClick(signupButton)
        observeLoginResult()

        return view
    }
    override fun onResume() {
        resetParameters()
        super.onResume()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        loginViewModel.clearLoginResult()
    }

    private fun observeLoginResult() {
        loginViewModel.loginResult.observe(viewLifecycleOwner) { result: Boolean ->
            if (result) {
                findNavController().navigate(R.id.action_loginFragment_to_postsFragment)
            }
            else {
                messageBox.visibility = View.VISIBLE
                messageBox.text = getString(R.string.invalidCreds)
            }
        }
    }

    private fun handleLoginClick(loginButton:Button) {
        loginButton.setOnClickListener {
            messageBox.visibility = View.INVISIBLE
            val credentials = UserCredentials(emailInput.text.toString(), passwordInput.text.toString())
            if (checkAllFields(credentials)) {
                loginViewModel.loginUser(credentials)
            }
        }
    }
    private fun handleSignUpClick(signupButton:Button) {
        signupButton.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
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

    private fun resetParameters(){
        emailInput.text.clear()
        passwordInput.text.clear()
        messageBox.text = ""
    }
}