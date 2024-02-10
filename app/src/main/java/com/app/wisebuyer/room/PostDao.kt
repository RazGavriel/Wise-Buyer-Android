package com.app.wisebuyer.room
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.wisebuyer.posts.Post

@Dao
interface PostDao {
    @Query("SELECT * FROM post ORDER BY createdAt desc")
    fun getAll(): List<Post>

    @Query("SELECT * FROM post WHERE userEmail = :email")
    fun getAllPostByUserEmail(email: String): List<Post>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(vararg post: Post)

}