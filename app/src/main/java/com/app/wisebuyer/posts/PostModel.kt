package com.app.wisebuyer.posts

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

data class Post(
    val title: String = "",
    val productType: ProductType = ProductType.OTHER,
    val description: String = "",
    val link: String = "",
    val price: String = "",
    var productPicture: String = "",
    var id: String = "",
    var thumbsUpUsers: List<String> = emptyList(),
    var thumbsDownUsers: List<String> = emptyList(),
    var userEmail: String = "",
    var createdAt: Long = Date().time
)

data class LikeRequestStatus(
    val postEmail: String,
    val thumbsUpUsers: List<String>,
    val thumbsDownUsers: List<String>,
    val holder: PostCardsAdapter.PostViewHolder
)