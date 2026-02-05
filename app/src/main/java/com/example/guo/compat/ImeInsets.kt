package com.example.guo.compat

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

fun setupImeInsets(rootView: View) {
    ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
        val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

        v.setPadding(
            systemBars.left,
            systemBars.top,
            systemBars.right,
            imeInsets.bottom,
        )
        insets
    }
}