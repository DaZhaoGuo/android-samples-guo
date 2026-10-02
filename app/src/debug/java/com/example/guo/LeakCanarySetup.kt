package com.example.guo

import android.app.Application
import android.content.pm.ApplicationInfo
import leakcanary.AppWatcher

object LeakCanarySetup {
    fun install(app: Application) {
        // 当前安装的这个包，有没有被标成可调试。
        //
        // and 做按位与，只保留两边都是 1 的那些位, eg.
        //
        // flags            0b....1010
        // FLAG_DEBUGGABLE  0b00000010
        // 按位与之后        0b00000010   != 0  → 可调试
        //
        // flags            0b....1000
        // FLAG_DEBUGGABLE  0b00000010
        // 按位与之后        0b00000000   == 0  → 不可调试
        //
        // 运算顺序是 (flags and FLAG_DEBUGGABLE) != 0。and 先得到「这一位还在不在」，再和 0 比。不是 0 就说明这一位是开着的

        // 这个标记来自安装包里的 android:debuggable。普通 Run 的 debug 包会带上它。Profile 打出来的包虽然仍是
        // debug 变体，但这个标记会被拿掉，所以这里是 false，LeakCanary 不会安装
        val debuggable = app.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        if (debuggable) {
            AppWatcher.manualInstall(app)
        }
    }
}
