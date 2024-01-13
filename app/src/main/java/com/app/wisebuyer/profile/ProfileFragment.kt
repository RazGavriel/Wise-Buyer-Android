package com.app.wisebuyer.profile

import ProfileViewModel
import android.net.Uri
import com.app.wisebuyer.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import com.app.wisebuyer.utils.RequestStatus

class ProfileFragment : Fragment() {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val args: ProfileFragmentArgs by navArgs()

    private lateinit var userProfileString: TextView
    private lateinit var changeProfilePictureButton: Button
    private lateinit var progressBarProfilePhoto: ProgressBar
    private lateinit var profileImage: ImageView
    private lateinit var userMetaData : UserMetaData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(
            R.layout.fragment_profile, container, false
        )

        userProfileString = view.findViewById<TextView>(R.id.user_profile_string)
        changeProfilePictureButton = view.findViewById<Button>(R.id.change_profile_picture_button)
        progressBarProfilePhoto = view.findViewById<ProgressBar>(R.id.progress_bar_profile_photo)
        profileImage = view.findViewById<ImageView>(R.id.profile_image)

        userMetaData = UserMetaData(args.firstName, args.lastName, args.email, args.profilePicture)

        initializeFirstName()
        observeShowProfilePhoto()
        observeUploadProfileImage()
        handleChangeProfilePicture()

        profileViewModel.getProfileImage(userMetaData)

        return view

    }

    private fun observeShowProfilePhoto() {
        profileViewModel.showProfilePhoto.observe(viewLifecycleOwner) { result: Uri? ->
            if (result is Uri)
            {
                Glide.with(this)
                .load(result)
                .into(profileImage)
            }
            else
            {
                profileImage.visibility = View.VISIBLE
                progressBarProfilePhoto.visibility = View.GONE
            }
        }
    }

    private fun initializeFirstName() {
        "${userMetaData.firstName}'s Profile".also { userProfileString.text = it }
    }

    private val pickImageContract = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { profileViewModel.uploadProfileImage(userMetaData, it) }
    }

    private fun handleChangeProfilePicture() {
        changeProfilePictureButton.setOnClickListener {
            pickImageContract.launch("image/*")
        }
    }

    private fun observeUploadProfileImage() {
        profileViewModel.uploadProfileImageResult.observe(viewLifecycleOwner) { result: RequestStatus ->
            when (result) {
                RequestStatus.SUCCESS -> {
                    profileImage.visibility = View.VISIBLE
                    progressBarProfilePhoto.visibility = View.GONE
                }
                RequestStatus.IN_PROGRESS -> {
                    profileImage.visibility = View.GONE
                    progressBarProfilePhoto.visibility = View.VISIBLE
                }
                RequestStatus.FAILURE -> {
                    Toast.makeText(requireContext(), "Upload failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
