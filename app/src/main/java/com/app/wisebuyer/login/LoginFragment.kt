package com.app.wisebuyer.login

import LoginViewModel
import com.app.wisebuyer.R
import android.os.Bundle
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
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
import com.app.wisebuyer.utils.checkCredentials
import kotlinx.coroutines.runBlocking

class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by activityViewModels()
    private lateinit var emailInput : EditText
    private lateinit var passwordInput : EditText
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button
    private lateinit var messageBox : TextView


    val db = com.google.firebase.Firebase.firestore

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

        db.collection("Users").document("a@a.com")
            .get()
            .addOnSuccessListener { result ->
                Log.d("APP", "${result.id} => ${result.data}")
            }
            .addOnFailureListener { exception ->
                Log.w("APP", "Error getting documents.", exception)
            }



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
                val firstName = result.first["firstName"]
                val lastName = result.first["lastName"]
                val profilePhoto = result.first["profilePhoto"]
                val email = result.second

//                var metaDataUser = runBlocking {  loginViewModel.getMetaDataUser(result.second) }
                // var direction = LoginFragmentDirections.actionLoginFragmentToProfileFragment()
                findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
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
            if (checkCredentials(credentials, emailInput, passwordInput)) {
                loginViewModel.loginUser(credentials)
            }
        }
    }
    private fun handleSignUpClick(signupButton:Button) {
        signupButton.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }

    private fun resetParameters(){
        emailInput.text.clear()
        passwordInput.text.clear()
        messageBox.text = ""

    }
}