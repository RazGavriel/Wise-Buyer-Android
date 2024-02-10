package com.app.wisebuyer.room

import android.content.Context
import androidx.room.Room

fun getWiseBuyerLocalDatabase(context:Context): WiseBuyerLocalDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        WiseBuyerLocalDatabase::class.java,
        "wiseBuyer_database"
    ).allowMainThreadQueries().build()
}
