package com.app.wisebuyer.room

import androidx.room.Database
import androidx.room.TypeConverters
import androidx.room.RoomDatabase
import com.app.wisebuyer.posts.Post

@Database(entities = [Post::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WiseBuyerLocalDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}