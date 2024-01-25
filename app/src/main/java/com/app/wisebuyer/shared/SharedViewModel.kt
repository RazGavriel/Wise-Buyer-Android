package com.app.wisebuyer.shared

import androidx.lifecycle.ViewModel
import com.app.wisebuyer.profile.UserMetaData

class SharedViewModel : ViewModel() {
    var userMetaData: UserMetaData = UserMetaData(firstName = "", lastName = "",
                                                  email = "", profilePhoto = "")
}