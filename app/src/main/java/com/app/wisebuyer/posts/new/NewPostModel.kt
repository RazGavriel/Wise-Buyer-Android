package com.app.wisebuyer.posts.new

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
    val title: String,
    val productType: ProductType,
    val description: String, val link: String, val price: String, var productPicture: String = "", var userEmail: String = "",
)