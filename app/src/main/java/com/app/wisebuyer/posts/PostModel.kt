package com.app.wisebuyer.posts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.app.wisebuyer.room.Converters
import java.util.Date

enum class ProductType(val type: String) {
    HOME("home"),
    ELECTRONICS("electronics"),
    CLOTHING("clothing"),
    BOOKS("books"),
    BEAUTY("beauty"),
    SPORTS("sports"),
    FURNITURE("furniture"),
    TOYS("toys"),
    FOOD("food"),
    AUTOMOTIVE("automotive"),
    JEWELRY("jewelry"),
    MUSIC("music"),
    PET_SUPPLIES("pet supplies"),
    HEALTH("health"),
    ART("art"),
    STATIONERY("stationery"),
    OUTDOORS("outdoors"),
    VIDEO_GAMES("video games"),
    SOFTWARE("software"),
    OTHER("other");

    companion object {
        fun fromString(type: String): ProductType {
            return entries.find { it.type.equals(type, ignoreCase = true) }
                ?: OTHER
        }
    }
}


@Entity
data class Post(
    @PrimaryKey
    var id: String = "",

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "productType")
    var productType: ProductType = ProductType.OTHER,

    @ColumnInfo(name = "description")
    var description: String = "",

    @ColumnInfo(name = "link")
    var link: String = "",

    @ColumnInfo(name = "price")
    var price: String = "",

    @ColumnInfo(name = "productPicture")
    var productPicture: String = "",

    @TypeConverters(Converters::class)
    @ColumnInfo(name = "thumbsUpUsers")
    var thumbsUpUsers: List<String> = emptyList(),

    @TypeConverters(Converters::class)
    @ColumnInfo(name = "thumbsDownUsers")
    var thumbsDownUsers: List<String> = emptyList(),

    @ColumnInfo(name = "userEmail")
    var userEmail: String = "",

    @ColumnInfo(name = "createdAt")
    var createdAt: Long = Date().time,

    @ColumnInfo(name = "lastUpdate")
    var lastUpdate: Long = Date().time,
) {
    @Ignore
    constructor() : this("", "", ProductType.OTHER, "", "", "", "", emptyList(), emptyList(), "", Date().time, Date().time)
}


data class LikeRequestStatus(
    val postEmail: String,
    val thumbsUpUsers: List<String>,
    val thumbsDownUsers: List<String>,
    val holder: PostCardsAdapter.PostViewHolder
)