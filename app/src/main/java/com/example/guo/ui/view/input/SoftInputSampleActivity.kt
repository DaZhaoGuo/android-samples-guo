package com.example.guo.ui.view.input

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.guo.compat.setupImeInsets
import com.example.guo.databinding.ActivitySoftInputSampleBinding

class SoftInputSampleActivity : AppCompatActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, SoftInputSampleActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivitySoftInputSampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoftInputSampleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupImeInsets(binding.root)
    }
}