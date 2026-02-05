package com.example.guo.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.example.guo.App

fun checkPermission(
    activity: Activity,
    onGranted: (() -> Unit)? = null,
) {
    if (ContextCompat.checkSelfPermission(App.mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED
    ) {
        // 第二步，如果没有授权，就授权
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            1,
        )
    } else {
        onGranted?.invoke()
    }
}

fun checkAudioPermission(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val permission = Manifest.permission.RECORD_AUDIO
        val granted =
            ContextCompat.checkSelfPermission(activity, permission)
        if (granted != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                1,
            )
        }
    }
}
