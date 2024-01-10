package com.app.wisebuyer.profile

import com.app.wisebuyer.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import android.util.Base64
import android.widget.Button
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage


class ProfileFragment : Fragment() {
    private lateinit var searchInput: EditText
    private lateinit var Mainbutton: Button
    lateinit var storage: FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(
            R.layout.fragment_profile, container, false
        )
        // Inflate the layout for this fragment

        storage = Firebase.storage
        searchInput = view.findViewById<EditText>(R.id.search_input)
        Mainbutton = view.findViewById<Button>(R.id.main_button)
        uploadBase64Image("iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAMAAACdt4HsAAABC1BMVEX////9/v38/vv7/fr6/fj5/Pf4/PX2+/P1+/L0+vDz+u/y+e3v+ert+Ofs9+Xr9+To9uHn9d/m9d3l9Nzk9Nrj9Nnh89fg89bf8tTd8dHc8c/a8M7Z8MzX78nW78fV7sbT7sTR7cHO67zL6rnJ6bbI6bTD567A5qm95aa65KK546C24p214pu04Zqz4Ziy4Jew4JWu35Kr3o2o3Yqn3Iil24Wk24Sg2n+e2XyY1nSX1nKU1W+T1G6R1GqP02eN0maM0mSK0WGJ0F+I0F6Gz1yFz1uEz1mDzliCzlaBzVR/zVN9zFB8y054ykl3ykh2yUZ1yUV0yENzyEJxx0Bvxj1uxjttxTpsxThrxTddvLrlAAABV0lEQVR42u3VWVOCUBjG8b/QQpZle7aXlZXZppK0qFlmZhZW5vv9P0kXiNkyDcfGu8MVPO/MDziHeUD+eaABDWhAAxroG1AFAHNsfveyK64kZoYNazpuNwICAMzm/TRl+NlkAMBxXbdWSq+BkfHCU4im83eF7NZIMgDg3/dmgpAjItIKs9T0suarAiBuBOtFRCpwrrKIHUCu4FhESlDsDZA5pkTkCU57BJLwLCILhMu9AQ4URKRgMLBf7wUogiMicjEKxobTUgbKkPM2ZG8EiGRUgTxctE+buRUg5qoBOSh9Xt2twrIakMB8657u/PJF/AW8R4h9mbohTlSALNhfxxYpBaA8yPj79yewgwNnQ5jXIiJPHWUbsx6oD+r3lwdRGHa8OrHidvHh8dZe4Ocb/NVI69V2H3US8zBgJ4asibWjip81MptTlmmGF5M1/WPRgAY0oIH+Ah8ZeC+T8aoVigAAAABJRU5ErkJggg==")

        return view
    }

//        private fun handlebuttonclick() {
//        Mainbutton.setOnClickListener {
//            // Get first and last name from your UI elements
//            val firstName = "DD" // Replace with actual data
//            val lastName = "S"   // Replace with actual data
//
//            lifecycleScope.launch(Dispatchers.Main) {
//                val avatarBytes = runBlocking{
//                    generateAvatar(firstName, lastName)
//                }
//                uploadBase64Image(avatarUrlBase64)
//
//                // Handle the avatarUrl as needed (e.g., update UI)
//                println("Generated Avatar URL: $avatarUrlBase64")
//            }
//        }
//    }
    fun uploadBase64Image(base64Image: String) {
        // Convert Base64 string to ByteArray
        val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)

        val linkToStorage = "https://console.firebase.google.com/u/0/project/wise-buyer-android-1ab6e/storage/wise-buyer-android-1ab6e.appspot.com/files"
        // Initialize Firebase Storage
        val storageRef = storage.reference
            .child("images/d52a4216-0acc-43d7-ba00-d3ffdeecc59b.jpg").putBytes(imageBytes).addOnCompleteListener{task ->
                if (task.isSuccessful)
                {
                    var a = "Dsadas"
                }
                else{
                    var b = "dasdas d"
                }

            }
    }
}
