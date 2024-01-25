package com.app.wisebuyer.profile

import ProfileViewModel
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.wisebuyer.MainActivity
import com.app.wisebuyer.R
import com.app.wisebuyer.posts.PostCardsAdapter
import com.app.wisebuyer.posts.PostViewModel
import com.app.wisebuyer.shared.PostBaseFragment
import com.app.wisebuyer.shared.SharedViewModel
import com.app.wisebuyer.singup.UserProperties
import com.app.wisebuyer.utils.RequestStatus
import com.app.wisebuyer.utils.isString
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProfileFragment : PostBaseFragment(), PostCardsAdapter.OnPostItemClickListener {
    private val profileViewModel: ProfileViewModel by viewModels()
    private val postViewModel: PostViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var addNewPostButton: FloatingActionButton
    private lateinit var userProfileString: TextView
    private lateinit var changeProfilePictureButton: Button
    private lateinit var progressBarProfilePhoto: ProgressBar
    private lateinit var profileImage: ImageView
    private lateinit var threeDotsMenu: ImageView

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_profile
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? = super.onCreateView(inflater, container, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        if (view != null) {
            initViews(view)
        }
        initializeUserName()
        handleChangeProfilePicture()
        handleAddNewClick()
        setupRecyclerView()
        handleChangeName()
        checkInitializationShareViewModel()

        observeShowProfilePhoto()
        observeChangeName()
        observeUploadProfileImage()
        observePostViewModel()
        observeRequestStatus()
        observeLikeRequestStatus()
        observeInitializeUserDataStatus()

        postViewModel.getPosts("userEmail", sharedViewModel.userMetaData.email)
        profileViewModel.getProfileImage(sharedViewModel.userMetaData)
        return view
    }

    private fun initViews(view: View) {
        userProfileString = view.findViewById<TextView>(R.id.user_profile_string)
        changeProfilePictureButton = view.findViewById<Button>(R.id.change_profile_picture_button)
        progressBarProfilePhoto = view.findViewById<ProgressBar>(R.id.progress_bar_profile_photo)
        profileImage = view.findViewById<ImageView>(R.id.profile_image)
        threeDotsMenu = view.findViewById<ImageView>(R.id.three_dots_menu)
        recyclerView = view.findViewById(R.id.posts_recycler_view)
        addNewPostButton = view.findViewById(R.id.add_new_post_button)
    }

    private fun observeInitializeUserDataStatus() {
        observeInitializeUserDataStatus(postViewModel)
    }

    private fun handleAddNewClick() {
        addNewPostButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_newPostFragment)
        }
    }

    private fun initializeUserName() {
        "${sharedViewModel.userMetaData.firstName}'s Profile".also { userProfileString.text = it }
        updateHeaderNavigationDrawer()
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
            val firstNameInput = dialogView.findViewById<EditText>(R.id.firstNameInput)
            val lastNameInput = dialogView.findViewById<EditText>(R.id.lastNameInput)
            val title = "Full Name - ${sharedViewModel.userMetaData.firstName} " +
                    "${sharedViewModel.userMetaData.lastName}\nChange to :"

            val spannableTitle = SpannableString(title)
            spannableTitle.setSpan(StyleSpan(Typeface.BOLD), 0,
                title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(spannableTitle)
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    val newFirstName = firstNameInput.text.toString()
                    val newLastName = lastNameInput.text.toString()
                    handleDialogNameValidation(UserProperties(newFirstName,newLastName))
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun handleDialogNameValidation(userProperties: UserProperties) {
        if (checkUserProperties(userProperties)) {
            val newUser = sharedViewModel.userMetaData
            newUser.firstName = userProperties.firstName
            newUser.lastName = userProperties.lastName
            profileViewModel.changeUserName(newUser)
        }
    }

    private fun checkUserProperties(userProperties: UserProperties): Boolean {
        if (userProperties.firstName.isEmpty() || !isString(userProperties.firstName)) {
            showDialogResponse("ERROR - Enter valid first name")
        } else if (userProperties.lastName.isEmpty() || !isString(userProperties.lastName)) {
            showDialogResponse("ERROR - Enter valid last name")
        } else {
            return true
        }
        return false
    }

    private fun observeChangeName() {
        profileViewModel.changeNameResult.observe(viewLifecycleOwner) { result: UserProperties? ->
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                if (result != null) {
                    sharedViewModel.userMetaData.firstName = result.firstName
                    sharedViewModel.userMetaData.lastName = result.lastName
                    showDialogResponse("Name changed successfully :)")
                    initializeUserName()
                    profileViewModel.changeNameResult.removeObservers(viewLifecycleOwner)

                } else {
                    showDialogResponse("Error while changing your name")
                }
            }
        }
    }

    private fun observeShowProfilePhoto() {
        profileViewModel.showProfilePhoto.observe(viewLifecycleOwner) { result: Uri? ->
            if (result is Uri) {
                Glide.with(this)
                    .load(result)
                    .into(profileImage)
            } else {
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
                else -> {
                }
            }
        }
    }
    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observePostViewModel() {
        observePostViewModel(postViewModel, recyclerView)
    }

    private fun observeRequestStatus() {
        observeRequestStatus(postViewModel)
    }

    private fun observeLikeRequestStatus() {
        observeLikeRequestStatus(postViewModel)
    }

}
