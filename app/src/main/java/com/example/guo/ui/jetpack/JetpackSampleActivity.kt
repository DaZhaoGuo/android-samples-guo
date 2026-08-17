package com.example.guo.ui.jetpack

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.guo.viewmodel.JetpackDemoViewModel
import com.example.guo.R

class JetpackSampleActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, JetpackSampleActivity::class.java)
            context.startActivity(intent)
        }
    }

    lateinit var viewModel: JetpackDemoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jetpack_sample)

        viewModel = ViewModelProviders.of(this).get(JetpackDemoViewModel::class.java)
        viewModel.plus()
        viewModel.testDelay()

        initObserver()
    }

    private fun initObserver() {
        viewModel.counter.observe(this@JetpackSampleActivity) {
            Log.d("TAG", "initObserver: ====$it")
            if (it != null) {
                Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }


}