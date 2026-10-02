package com.example.guo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.example.guo.ui.largescreen.embedding.SplitManager
import com.tencent.mmkv.MMKV

/**
 * 在创建应用进程时，应用子类将是实例化的第一个类
 */
class App : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this

        // RuleController可有可无
//        RuleController.getInstance(this)
//            .setRules(RuleController.parseRules(this, R.xml.main_split_config))
        LeakCanarySetup.install(this)
        SplitManager.createSplit() // WindowManager api管理分屏

        // /data/user/0/package/files/mmkv
        MMKV.initialize(this)
        initARouter()
    }

    private fun initARouter() {
        //启动优化－开始抓取TraceView
        //        Debug.startMethodTracing("trace_view_start");

        //        if (isDebug()) {           // These two lines must be written before init, otherwise these configurations will be invalid in the init process
        ARouter.openLog() // Print log
        ARouter.openDebug() // Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version needs to be closed, otherwise there is a security risk)

        //        }
        ARouter.init(this) // As early as possible, it is recommended to initialize in the Application
    }
}