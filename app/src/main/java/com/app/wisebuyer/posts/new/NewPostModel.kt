package com.app.wisebuyer.posts.new

import android.text.Editable

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
}

data class Post(
    val title: String,
    val productType: ProductType,
    val description: String, val link: String, val price: Number, val productPicture: String
)