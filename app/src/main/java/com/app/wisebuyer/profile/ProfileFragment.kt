package com.app.wisebuyer.profile

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import com.app.wisebuyer.R
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.app.wisebuyer.MainActivity
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import android.Manifest
import android.app.Activity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {

    private lateinit var UserProfileString: TextView
    private lateinit var changeProfilePictureButton: Button

    private lateinit var profileImage: ImageView

    private val args: ProfileFragmentArgs by navArgs()
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var profilePicture: String
    private val PICK_IMAGE_REQUEST = 1

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

        var progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Uploading...")
        progressDialog.setCancelable(false)

        InitializeFirstName()
        initializeProfileImage()
        handleChangeProfilePicture()
        return view

    }

    private fun handleChangeProfilePicture() {
        changeProfilePictureButton.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }

    private fun initializeProfileImage() {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri: Uri = data.data!!
            uploadImageToFirebaseStorage(imageUri)
        }
    }

    private fun uploadImageToFirebaseStorage(imageUri: Uri) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val imageRef: StorageReference = FirebaseStorage.getInstance().getReference(profilePicture)

        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                initializeProfileImage()
                // Image uploaded successfully
                // You can handle success here, for example, update the user's profile with the new image URL
                // UpdateUserProfile(imageRef.downloadUrl.toString())
            }
            .addOnFailureListener { e ->
                // Handle failures
            }
    }
}
