package com.example.guo.exception.anr

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.guo.R
import com.example.guo.databinding.ActivityExceptionSampleBinding

class ExceptionSampleActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, ExceptionSampleActivity::class.java))
        }
    }


    private lateinit var binding: ActivityExceptionSampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExceptionSampleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.initListener()
    }

    private fun ActivityExceptionSampleBinding.initListener() {
        btnAnr.setOnClickListener { ANRSampleActivity.launch(this@ExceptionSampleActivity) }
    }
}