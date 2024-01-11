package com.app.wisebuyer.profile

import android.annotation.SuppressLint
import com.app.wisebuyer.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage


class ProfileFragment : Fragment() {

    private lateinit var UserProfileString: TextView
    private lateinit var changeProfilePictureButton: Button
    private lateinit var profileImage: ImageView

    private lateinit var firstName:String
    private lateinit var lastName:String
    private lateinit var email:String
    private lateinit var profilePicture:String

    private  val args: ProfileFragmentArgs by navArgs()
    val storage = FirebaseStorage.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(
            R.layout.fragment_profile, container, false
        )

        UserProfileString = view.findViewById<TextView>(R.id.user_profile_string)
        changeProfilePictureButton = view.findViewById<Button>(R.id.change_profile_picture_button)
        profileImage = view.findViewById<ImageView>(R.id.profile_image)
        firstName = args.firstName
        lastName = args.lastName
        email = args.email
        profilePicture = args.profilePicture

        InitializeFirstName()
        InitializeProfileImage()

        return view
    }

    fun InitializeProfileImage() {
        val storage = FirebaseStorage.getInstance()
        val gsReference = storage.reference.child(profilePicture)

        gsReference.downloadUrl
            .addOnSuccessListener { uri ->
                Glide.with(this)
                    .load(uri)
                    .into(profileImage)
            }
            .addOnFailureListener { exception ->
                Log.e("APP", "Error loading profile image: ${exception.message}")
            }
    }

    @SuppressLint("SetTextI18n")
    fun InitializeFirstName() {
        UserProfileString.text = "$firstName's Profile"
    }



}
