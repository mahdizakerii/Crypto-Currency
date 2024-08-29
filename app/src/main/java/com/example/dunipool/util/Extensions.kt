package com.example.dunipool.util

import android.content.Context
import android.widget.Toast

fun Context.showToast(str :String) {
    Toast.makeText(this , str , Toast.LENGTH_LONG).show()
}