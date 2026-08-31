package com.example.guo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.alibaba.android.arouter.launcher.ARouter
import com.bagguo.lib_base.constan.RouterTable
import com.example.guo.async.AsyncActivity
import com.example.guo.ui.view.animation.AnimationActivity
import com.example.guo.communication.CommunicateActivity
import com.example.guo.di.dagger.DaggerActivity
import com.example.guo.databinding.ActivityMainBinding
import com.example.guo.storage.db.sqllite.SqliteTestActivity
import com.example.guo.ui.view.dispatchevent.DispatchEventDemoActivity
import com.example.guo.communication.ipc.IPCTestActivity
import com.example.guo.ui.jetpack.JetpackSampleActivity
import com.example.guo.ui.jni.JNISampleActivity
import com.example.guo.memory.MemorySamplesActivity
import com.example.guo.net.retrofit.RetrofitTest
import com.example.guo.net.rxjava.RxJavaTest
import com.example.guo.net.rxjavaretrofit.RxjavaRetrofitTest
import com.example.guo.ui.service.StartServiceTestActivity
import com.example.guo.ui.UIEntryActivity
import com.example.guo.debug.DebugToolsManager
import com.example.guo.exception.anr.ExceptionSampleActivity
import com.example.guo.service.start.ClipboardMonitorService
import com.example.guo.ui.live.HeartFlowActivity
import com.example.guo.wallet.WalletActivity
import com.example.guo.ui.webview.JavaJSCallActivity
import com.example.guo.ui.webview.WebViewDemoActivity

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"

    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ======")

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.tv.text = "ViewBinding绑定的view"

        checkPermission()

        RxJavaTest().test(this)
        RetrofitTest().test()
        RxjavaRetrofitTest().test()

        binding.loginBtn.setOnClickListener {
            ARouter.getInstance().build(RouterTable.Login.LOGIN)
                .navigation()
        }

        binding.btnAsync.setOnClickListener {
            // TODO: 组件内router失败
//            ARouter.getInstance().build(RouterTable2.Main.ASYNC)
//                .navigation()

            AsyncActivity.start(this)
        }
        binding.btnJetpack.setOnClickListener { JetpackSampleActivity.start(this) }
        binding.btnLive.setOnClickListener { HeartFlowActivity.createIntent(this) }
        binding.btnUi.setOnClickListener { UIEntryActivity.start(this) }
        binding.btnMemory.setOnClickListener { MemorySamplesActivity.start(this) }
        binding.btnMainAnimation.setOnClickListener { AnimationActivity.start(this) }
        binding.btnMainDispatchEvent.setOnClickListener { DispatchEventDemoActivity.start(this) }
        binding.btnCommunication.setOnClickListener { CommunicateActivity.start(this) }
        binding.btnService.setOnClickListener { StartServiceTestActivity.start(this) }
        binding.btnDagger.setOnClickListener { DaggerActivity.start(this) }
        binding.btnIpc.setOnClickListener { IPCTestActivity.start(this) }
        binding.btnDb.setOnClickListener { SqliteTestActivity.start(this) }
        binding.btnWebView.setOnClickListener { WebViewDemoActivity.start(this) }
        binding.btnJavaJsCall.setOnClickListener { JavaJSCallActivity.start(this) }
        binding.btnJni.setOnClickListener { JNISampleActivity.start(this) }
        findViewById<View>(R.id.btn_wallet).setOnClickListener { WalletActivity.start(this@MainActivity) }
        binding.btnException.setOnClickListener { ExceptionSampleActivity.launch(this) }


        yufa()

        hanshu()

        setParams()

        startService(
            Intent(this, ClipboardMonitorService::class.java)
        )

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: =====")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: ======")
    }

    override fun onResume() { //activity不可见
        super.onResume()
        Log.d(TAG, "onResume: =======")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ======")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ======")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ======")
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        //启动优化－抓取TraceView结束点
//        Debug.stopMethodTracing();
    }

    private fun setParams() {
        DebugToolsManager.createDebugFloat(this)
    }

    //let、with、run、apply、also函数
    private fun hanshu() {

        //also函数使用的一般结构
        //object.also{
        ////todo
        //}
        //适用于let函数的任何场景，also函数和let很像，只是唯一的不同点就是let函数最后的返回值是最后一行的返回值而also函数的返回值是返回当前的这个对象。一般可用于多个扩展函数链式调用

        //apply函数使用的一般结构
        //object.apply{
        ////todo
        //}
        //整体作用功能和run函数很像，唯一不同点就是它返回的值是对象本身，而run函数是一个闭包形式返回，返回的是最后一行的值。正是基于这一点差异它的适用场景稍微与run函数有点不一样。apply一般用于一个对象实例初始化的时候，需要对对象中的属性进行赋值。或者动态inflate出一个XML的View的时候需要给View绑定数据也会用到，这种情景非常常见。特别是在我们开发中会有一些数据model向View model转化实例化的过程中需要用到。


        //run函数使用的一般结构
        //object.run{
        ////todo
        //}
        //适用于let,with函数任何场景。因为run函数是let,with两个函数结合体，准确来说它弥补了let函数在函数体内必须使用it参数替代对象，在run函数中可以像with函数一样可以省略，直接访问实例的公有属性和方法，另一方面它弥补了with函数传入对象判空问题，在run函数中可以像let函数一样做判空处理


        //with函数使用的一般结构
        //with(object){
        //   //todo
        // }
        //适用于调用同一个类的多个方法时，可以省去类名重复，直接调用类的方法即可，经常用于Android中RecyclerView中onBinderViewHolder中，数据model的属性映射到UI上


        //内联扩展函数之let
        //let扩展函数的实际上是一个作用域函数，当你需要去定义一个变量在一个特定的作用域范围内，let函数的是一个不错的选择；let函数另一个作用就是可以避免写一些判断null的操作。
        //let函数适用的场景
        //场景一: 最常用的场景就是使用let函数处理需要针对一个可null的对象统一做判空处理。
        //
        //场景二: 然后就是需要去明确一个变量所处特定的作用域范围内可以使用
        //object.let{
        //   it.todo()//在函数体内使用it替代object对象去访问其公有的属性和方法
        //   ...
        //}

        ////另一种用途 判断object为null的操作
        //object?.let{//表示object不为null的条件下，才会去执行let函数体
        //   it.todo()
        //}

        //let函数底层的inline扩展函数+lambda结构
        //@kotlin.internal.InlineOnly
        //public inline fun <T, R> T.let(block: (T) -> R): R = block(this)


        //call back
        //mView.setEventListener(new ExamPlanHomeEventListener(){
        //
        //    public void onSuccess(Data data){
        //      //todo
        //    }
        //
        // });

        //mView.setEventListener(object: ExamPlanHomeEventListener{
        //
        //    public void onSuccess(Data data){
        //      //todo
        //    }
        //
        //});

        //mView.setEventListener({
        //   data: Data ->
        //   //todo
        //})
        //
        ////或者可以直接省略Data,借助kotlin的智能类型推导
        //
        //mView.setEventListener({
        //   data ->
        //   //todo
        //})

        //mView.setEventListener({
        //  //todo
        //
        //})

        //mView.setEventListener(){
        //   //todo
        //}

        //mView.setEventListener{
        //  //todo
        //}

    }

    //语法=========
    private fun yufa() {
        //？加在变量声明类型后表示当前变量可以为空
        var a: String? = null

        //?加在变量后调用方法前表示当变量为空是去调用方法也不会报错
        Log.d("TAG", "onCreate: a lenght:" + a?.length)

        //!!表示会对当前的变量或者对象进行非空判断，如果变量为空则会报空指针异常
        //        Log.d("TAG", "onCreate: a lenght:" + a!!.length)


        //if
        val b: Int = 10
        val c: Int = 20

        if (c > b) {
            Log.d("TAG", "yufa: ====b > c")
        }


        when (c) {
            1 -> {
                println("c = 1")
            }

            20 -> {
                println("c = 10")
            }

            else -> {
                println("c != 1也 != 10")
            }
        }
    }

    val stringLengthFunc: (String) -> Int = { input ->
        input.length
    }
    val stringLength: Int = stringLengthFunc("Android")

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            }
        }
    }
}