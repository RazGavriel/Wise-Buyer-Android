package com.app.wisebuyer.utils

import android.util.Base64
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.util.Random
import java.util.UUID;


public suspend fun generateAvatar(firstName: String, lastName: String): ByteArray {
    try {
        val apiUrl = "https://ui-avatars.com/api/"
        val randomBackgroundColor = generateRandomColor()

        val url2 = "$apiUrl?background=$randomBackgroundColor&color=fff&name=${firstName}+${lastName}"

        val url: URL = URI.create(url2).toURL()

        val responseCode: Int = withContext(Dispatchers.IO) {
            (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
            }.responseCode
        }

        if (responseCode == HttpURLConnection.HTTP_OK) {
            val response: ByteArray = withContext(Dispatchers.IO) {
                (url.openConnection() as HttpURLConnection).inputStream.use {
                    BufferedInputStream(it).readBytes()
                }
            }
            return response
        } else {
            return byteArrayOf(0)
        }
    } catch (e: Exception) {
        return byteArrayOf(0)
    }
}

public fun generateRandomColor(): String {
    val random = Random()
    val color = String.format("%06x", random.nextInt(0xFFFFFF + 1))
    return color
}

public fun uploadBytesImage(storage: FirebaseStorage, fileName:String, fileContent:ByteArray) : Boolean {
    var returnValue = false

    val fileContent = Base64.decode("iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAMAAACdt4HsAAABC1BMVEX////9/v38/vv7/fr6/fj5/Pf4/PX2+/P1+/L0+vDz+u/y+e3v+ert+Ofs9+Xr9+To9uHn9d/m9d3l9Nzk9Nrj9Nnh89fg89bf8tTd8dHc8c/a8M7Z8MzX78nW78fV7sbT7sTR7cHO67zL6rnJ6bbI6bTD567A5qm95aa65KK546C24p214pu04Zqz4Ziy4Jew4JWu35Kr3o2o3Yqn3Iil24Wk24Sg2n+e2XyY1nSX1nKU1W+T1G6R1GqP02eN0maM0mSK0WGJ0F+I0F6Gz1yFz1uEz1mDzliCzlaBzVR/zVN9zFB8y054ykl3ykh2yUZ1yUV0yENzyEJxx0Bvxj1uxjttxTpsxThrxTddvLrlAAABV0lEQVR42u3VWVOCUBjG8b/QQpZle7aXlZXZppK0qFlmZhZW5vv9P0kXiNkyDcfGu8MVPO/MDziHeUD+eaABDWhAAxroG1AFAHNsfveyK64kZoYNazpuNwICAMzm/TRl+NlkAMBxXbdWSq+BkfHCU4im83eF7NZIMgDg3/dmgpAjItIKs9T0suarAiBuBOtFRCpwrrKIHUCu4FhESlDsDZA5pkTkCU57BJLwLCILhMu9AQ4URKRgMLBf7wUogiMicjEKxobTUgbKkPM2ZG8EiGRUgTxctE+buRUg5qoBOSh9Xt2twrIakMB8657u/PJF/AW8R4h9mbohTlSALNhfxxYpBaA8yPj79yewgwNnQ5jXIiJPHWUbsx6oD+r3lwdRGHa8OrHidvHh8dZe4Ocb/NVI69V2H3US8zBgJ4asibWjip81MptTlmmGF5M1/WPRgAY0oIH+Ah8ZeC+T8aoVigAAAABJRU5ErkJggg==", Base64.DEFAULT)
    val storageRef = storage.reference
        .child("profilePictures/$fileName.jpg")
        .putBytes(fileContent)
        .addOnCompleteListener {task->
            if (task.isSuccessful){
                returnValue = true
            }
            else{
                var asd = "dsad"
            }
        }
    return returnValue
}

