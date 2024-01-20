package com.app.wisebuyer.utils

import android.view.View

fun manageViews(vararg views: View, mode: String) {
    for (view in views) {
        if (mode == "GONE") { view.visibility = View.GONE }
        else{ view.visibility = View.VISIBLE }
    }
}