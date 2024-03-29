package com.app.wisebuyer.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

fun manageViews(vararg views: View, mode: String) {
    for (view in views) {
        if (mode == "GONE") { view.visibility = View.GONE }
        else{ view.visibility = View.VISIBLE }
    }
}

fun closeKeyboard(context: Context, view:View) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun showDialogResponse(message: String, rootView: View) {
    val snackBar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
    val snackBarView: View = snackBar.view
    snackBarView.setBackgroundColor(0xFF000000.toInt()) // black color
    val textView: TextView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text)
    textView.setTextColor(0xFFFFFFFF.toInt()) // white color
    snackBar.show()
}