package com.khaled.elmenus

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.khaled.elmenus.common.BaseActivity

class MainActivity : BaseActivity<MainViewModel>() {

    override val loadingView: View?
        get() = null

    override fun getCurrentActivity() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main)
    }
}