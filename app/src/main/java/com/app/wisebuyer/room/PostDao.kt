package com.app.wisebuyer.room
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.app.wisebuyer.posts.Post

@Dao
interface PostDao {
    @Query("SELECT * FROM post")
    fun getAll(): List<Post>
    @Insert
    fun insertAll(vararg post: Post)

}