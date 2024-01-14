package com.app.wisebuyer.profile

import ProfileViewModel
import android.app.AlertDialog
import android.graphics.Typeface
import android.net.Uri
import com.app.wisebuyer.R
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.app.wisebuyer.MainActivity
import com.app.wisebuyer.shared.SharedViewModel
import com.app.wisebuyer.utils.RequestStatus
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfileFragment : Fragment() {
    private val profileViewModel: ProfileViewModel by activityViewModels()
//    private val args: ProfileFragmentArgs by navArgs()

    private lateinit var userProfileString: TextView
    private lateinit var changeProfilePictureButton: Button
    private lateinit var progressBarProfilePhoto: ProgressBar
    private lateinit var profileImage: ImageView
    private lateinit var threeDotsMenu: ImageView
    private lateinit var sharedViewModel: SharedViewModel

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
        threeDotsMenu = view.findViewById<ImageView>(R.id.three_dots_menu)


        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        initializeFirstName()
        observeShowProfilePhoto()
        observeUploadProfileImage()
        handleChangeProfilePicture()
        handleChangeName()

        profileViewModel.getProfileImage(sharedViewModel.userMetaData)

        return view

    }

    private fun initializeFirstName() {
        "${sharedViewModel.userMetaData.firstName}'s Profile".also { userProfileString.text = it }
    }

    private val pickImageContract = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { profileViewModel.uploadProfileImage(sharedViewModel.userMetaData, it) }
    }

    private fun handleChangeProfilePicture() {
        changeProfilePictureButton.setOnClickListener {
            pickImageContract.launch("image/*")
        }
    }


    private fun handleChangeName() {
        threeDotsMenu.setOnClickListener {
            val dialogView = LayoutInflater.from(requireContext())
                                           .inflate(R.layout.dialog_change_name, null)
            val etFirstName = dialogView.findViewById<EditText>(R.id.editTextFirstName)
            val etLastName = dialogView.findViewById<EditText>(R.id.editTextLastName)

            val title = "Full Name - ${sharedViewModel.userMetaData.firstName} " +
                        "${sharedViewModel.userMetaData.lastName}\nChange to :"
            val spannableTitle = SpannableString(title)
            spannableTitle.setSpan(StyleSpan(Typeface.BOLD), 0,
                                   title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(spannableTitle)
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    val newFirstName = etFirstName.text.toString()
                    val newLastName = etLastName.text.toString()


                }
                .setNegativeButton("Cancel", null)
                .show()
        }
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
