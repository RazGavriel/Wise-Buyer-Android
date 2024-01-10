package com.app.wisebuyer.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.util.Random


suspend fun generateAvatar(firstName: String, lastName: String): ByteArray {
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
private fun generateRandomColor(): String {
    val random = Random()
    val color = String.format("%06x", random.nextInt(0xFFFFFF + 1))
    return color
}


