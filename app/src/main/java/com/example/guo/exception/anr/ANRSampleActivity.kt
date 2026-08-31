package com.example.guo.exception.anr

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.guo.R
import com.example.guo.databinding.ActivityAnrSampleBinding
import com.example.guo.databinding.ActivityExceptionSampleBinding

class ANRSampleActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, ANRSampleActivity::class.java))
        }
    }

    private lateinit var binding: ActivityAnrSampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAnrSampleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        test1()
    }

    private fun test1() {

        // 执行时序
        //
        //test1() 启动一个子线程执行 testANR()
        //主线程 SystemClock.sleep(10)，让子线程先跑起来
        //子线程进入 testANR()，拿到 this 的锁，然后 sleep(30_000)（持锁睡眠 30 秒）
        //10ms 后主线程调用 initView()，因为也是 @Synchronized，需要拿同一把锁 —— 但锁被子线程占着，主线程被阻塞等待锁，一等就是接近 30 秒
        Thread(Runnable() {
            testANR()
        }).start()

        SystemClock.sleep(10) // sleep 10ms，确保testANR先获得锁
        initView()
    }

    @Synchronized
    private fun testANR() {
        SystemClock.sleep(30 * 1000)
    }

    @Synchronized
    private fun initView() {
        binding.tv.text = "initView"
    }
}