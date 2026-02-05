package com.example.guo.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.guo.R
import com.example.guo.databinding.ActivityUiEntryBinding
import com.example.guo.ui.activity.ActivitySamplesActivity
import com.example.guo.ui.anim.SlideInOutSampleActivity
import com.example.guo.ui.chapter_3.ViewActivity
import com.example.guo.ui.dialog.DialogSampleActivity
import com.example.guo.ui.dynamic.DynamicLayoutActivity
import com.example.guo.ui.fragment.FragmentSamplesActivity
import com.example.guo.ui.fragment.bottomsheet.StickyBottomSheetFragment
import com.example.guo.ui.fragment.bottomsheet.TabVP2BottomSheetDialogFragment
import com.example.guo.ui.largescreen.BigScreenSamplesActivity
import com.example.guo.ui.multitype.normal.NormalActivity
import com.example.guo.ui.sidebar.SideBarActivity
import com.example.guo.ui.video.VideoEntryActivity
import com.example.guo.ui.view.customview.CustomViewSimpleActivity
import com.example.guo.ui.view.immersive.ImmersiveStatusBarActivity
import com.example.guo.ui.view.input.SoftInputActivity
import com.example.guo.ui.view.input.SoftInputSampleActivity
import com.example.guo.ui.view.layout.constraintlayout.ConstraintSampleActivity
import com.example.guo.ui.view.layout.gridview.GridViewDemoActivity
import com.example.guo.ui.view.layout.listview.ListViewDemoActivity
import com.example.guo.ui.view.layout.recyclerview.RecyclerViewEntryActivity
import com.example.guo.ui.view.layout.tablayout.IndicatorActivity
import com.example.guo.ui.view.layout.tablayout.TabLayoutSimpleActivity
import com.example.guo.ui.view.layout.tablayout.customtab.TabLayoutDemoActivity
import com.example.guo.ui.view.layout.textview.TextViewSampleActivity
import com.example.guo.ui.view.layout.toolbar.ToolbarSimpleActivity
import com.example.guo.ui.view.loading.LoadingActivity
import com.example.guo.ui.view.toast.ToastSampleActivity
import com.example.guo.ui.view.topbar.CollapsingPinedSampleActivity
import com.example.guo.ui.view.topbar.TopbarSamplesActivity
import com.example.guo.util.callSystemShare

class UIEntryActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "UIEntryActivity"

        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, UIEntryActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var mBinding: ActivityUiEntryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityUiEntryBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.initView()
    }

    private fun ActivityUiEntryBinding.initView() {
        btnActivity.setOnClickListener {
            ActivitySamplesActivity.launch(this@UIEntryActivity)
        }

        btnFgm.setOnClickListener {
            FragmentSamplesActivity.launch(this@UIEntryActivity)
        }

        btnTabVp2BottomSheetFragment.setOnClickListener {
            TabVP2BottomSheetDialogFragment
                .newInstance()
                .show(supportFragmentManager, "TabVP2BottomSheetDialogFragment")
        }

        btnStickBottomSheetFragment.setOnClickListener {
            StickyBottomSheetFragment
                .newInstance()
                .show(supportFragmentManager, "StickyBottomSheetFragment")
        }

        btnAnim.setOnClickListener {
            SlideInOutSampleActivity.launch(this@UIEntryActivity)
        }

        btnRecyclerView.setOnClickListener { RecyclerViewEntryActivity.start(this@UIEntryActivity) }
        btnConstraintLayout.setOnClickListener { ConstraintSampleActivity.start(this@UIEntryActivity) }
        btnCircleView.setOnClickListener { CircleViewSampleActivity.start(this@UIEntryActivity) }
        btnCustomViewSimple.setOnClickListener { CustomViewSimpleActivity.start(this@UIEntryActivity) }
        btnWeChatWaveView.setOnClickListener {  }
        btnView.setOnClickListener { ViewActivity.start(this@UIEntryActivity) }

        findViewById<View>(R.id.toolbar_simple).setOnClickListener { view: View? ->
            val intent =
                Intent(
                    this@UIEntryActivity,
                    ToolbarSimpleActivity::class.java,
                )
            startActivityForResult(intent, 1)
        }

        findViewById<View>(R.id.btn_list_view).setOnClickListener { view: View? ->
            val intent =
                Intent(
                    this@UIEntryActivity,
                    ListViewDemoActivity::class.java,
                )
            startActivity(intent)
        }

        findViewById<View>(R.id.btn_grid_view).setOnClickListener { view: View? ->
            val intent =
                Intent(
                    this@UIEntryActivity,
                    GridViewDemoActivity::class.java,
                )
            startActivity(intent)
        }

        findViewById<View>(R.id.btn_tab_layout).setOnClickListener {
            TabLayoutSimpleActivity.start(this@UIEntryActivity)
        }

        findViewById<View>(R.id.btn_tab_layout_custom).setOnClickListener { view: View? ->
            val intent =
                Intent(
                    this@UIEntryActivity,
                    TabLayoutDemoActivity::class.java,
                )
            startActivity(intent)
        }

        findViewById<View>(R.id.btn_tab_indicator).setOnClickListener {
            IndicatorActivity.start(this@UIEntryActivity)
        }

        findViewById<View>(R.id.btn_butter_knife).setOnClickListener { view: View? ->
            val intent =
                Intent(
                    this@UIEntryActivity,
                    ButterKnifeTestActivity::class.java,
                )
            startActivity(intent)
        }

        findViewById<View>(R.id.btn_multi_type).setOnClickListener { view: View? ->
            val intent =
                Intent(
                    this@UIEntryActivity,
                    NormalActivity::class.java,
                )
            startActivity(intent)
        }

        findViewById<View>(R.id.btn_dialog).setOnClickListener { view: View? ->
            val intent =
                Intent(
                    this@UIEntryActivity,
                    DialogSampleActivity::class.java,
                )
            startActivity(intent)
        }

        btnToast.setOnClickListener { ToastSampleActivity.launch(this@UIEntryActivity) }

        findViewById<View>(R.id.btn_dynamic_layout).setOnClickListener { view: View? ->
            val intent =
                Intent(
                    this@UIEntryActivity,
                    DynamicLayoutActivity::class.java,
                )
            startActivity(intent)
        }

        btnTextView.setOnClickListener { TextViewSampleActivity.start(this@UIEntryActivity) }
        btnLoading.setOnClickListener { LoadingActivity.start(this@UIEntryActivity) }
        btnSideBar.setOnClickListener { SideBarActivity.start(this@UIEntryActivity) }
        btnTopBar.setOnClickListener { TopbarSamplesActivity.launch(this@UIEntryActivity) }
        btnSoftInput.setOnClickListener { SoftInputSampleActivity.start(this@UIEntryActivity) }
        btnSoftInput2.setOnClickListener { SoftInputActivity.launch(this@UIEntryActivity) }
        btnImmersiveStatusBar.setOnClickListener { ImmersiveStatusBarActivity.start(this@UIEntryActivity) }
        btnVideo.setOnClickListener { VideoEntryActivity.launch(this@UIEntryActivity) }

        btnSystemShare.setOnClickListener {
            "www.baidu.com".callSystemShare(this@UIEntryActivity)
        }

        btnBigScreen.setOnClickListener {
            BigScreenSamplesActivity.launch(this@UIEntryActivity)
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            finish()
            Log.d(TAG, "onActivityResult: ====")
        }
    }
}
