package com.app.wisebuyer.login

import LoginViewModel
import com.app.wisebuyer.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.wisebuyer.MainActivity
import com.app.wisebuyer.shared.SharedViewModel
import com.app.wisebuyer.singup.UserProperties
import com.app.wisebuyer.utils.checkCredentials
import com.app.wisebuyer.utils.closeKeyboard
import com.app.wisebuyer.utils.manageViews

class LoginFragment : Fragment() {
    private val loginViewModel: LoginViewModel by activityViewModels()
    private lateinit var emailInput : EditText
    private lateinit var passwordInput : EditText
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button
    private lateinit var messageBox : TextView
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var progressBarLogin: ProgressBar

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
        progressBarLogin = view.findViewById<ProgressBar>(R.id.progress_bar_login)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

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
        loginViewModel.loginResult.observe(viewLifecycleOwner) { result: Pair<HashMap<String,Any>, String> ->
            if (result.first.isNotEmpty()) {
                updateSharedViewModel(result)

                (activity as MainActivity).updateHeaderUserName(
                    UserProperties(sharedViewModel.userMetaData.firstName,
                                   sharedViewModel.userMetaData.lastName))
                closeKeyboard(requireContext(), requireView())
                findNavController().navigate(R.id.action_loginFragment_to_postsFragment)
            }
            else {
                manageViews(emailInput, passwordInput, loginButton, signupButton,
                            messageBox, mode="VISIBLE")
                messageBox.visibility = View.VISIBLE
                messageBox.text = getString(R.string.invalidCreds)
            }
            progressBarLogin.visibility = View.GONE
        }
    }

    private fun updateSharedViewModel(result: Pair<HashMap<String,Any>, String>){
        sharedViewModel.userMetaData.email = result.second
        sharedViewModel.userMetaData.firstName = result.first["firstName"].toString()
        sharedViewModel.userMetaData.lastName = result.first["lastName"].toString()
        sharedViewModel.userMetaData.profilePhoto = result.first["profilePhoto"].toString()
    }

    private fun handleLoginClick(loginButton:Button) {
        loginButton.setOnClickListener {
            messageBox.visibility = View.INVISIBLE
            val credentials = UserCredentials(emailInput.text.toString(), passwordInput.text.toString())
            if (checkCredentials(credentials, emailInput, passwordInput)) {
                manageViews(emailInput, passwordInput, loginButton, signupButton,
                            messageBox, mode="GONE")
                progressBarLogin.visibility = View.VISIBLE
                loginViewModel.loginUser(credentials)
            }
        }
    }
    private fun handleSignUpClick(signupButton:Button) {
        signupButton.setOnClickListener{
            closeKeyboard(requireContext(), requireView())
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }

    private fun resetParameters(){
        emailInput.text.clear()
        passwordInput.text.clear()
        messageBox.text = ""
    }
}