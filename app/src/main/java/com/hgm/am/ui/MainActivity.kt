package com.hgm.am.ui

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.textfield.TextInputEditText
import com.hgm.am.R
import com.hgm.am.util.Constant.Companion.PERMISSIONS_STORAGE
import com.hgm.am.util.Constant.Companion.REQUEST_PERMISSION_CODE
import com.hgm.am.databinding.ActivityMainBinding
import com.hgm.am.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

      private val binding: ActivityMainBinding by lazy {
            ActivityMainBinding.inflate(layoutInflater)
      }
      lateinit var viewModel: MainViewModel

      override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(binding.root)

            //启动获取权限
            getPermission(this)

            //初始化导航
            val navController =
                  (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment).navController
            binding.bottomNavigationView.setupWithNavController(navController)

            //...
            viewModel = ViewModelProvider(this)[MainViewModel::class.java]
      }


      /**
       * 动态获取权限
       */
      private fun getPermission(obj: Activity) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                  for (i in PERMISSIONS_STORAGE.indices) {
                        if (ActivityCompat.checkSelfPermission(
                                    obj,
                                    PERMISSIONS_STORAGE[i]
                              ) != PackageManager.PERMISSION_GRANTED
                        ) {
                              ActivityCompat.requestPermissions(
                                    obj,
                                    PERMISSIONS_STORAGE,
                                    REQUEST_PERMISSION_CODE
                              )
                        }
                  }
            }
      }


      override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
            if (ev?.action == MotionEvent.ACTION_DOWN) {
                  val view = currentFocus
                  if (isHideInput(view, ev)) {
                        val imm =
                              getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view?.windowToken, 0)
                  }
                  return super.dispatchTouchEvent(ev)
            }
            if (window.superDispatchTouchEvent(ev)) {
                  return true
            }
            return onTouchEvent(ev)
      }

      /**
       * 判断是否隐藏
       */
      private fun isHideInput(view: View?, event: MotionEvent?): Boolean {
            if (view != null && view is TextInputEditText) {
                  //获取输入框在屏幕的位置
                  val left = 0
                  val top = 0
                  val bottom = top + view.height
                  val right = left + view.width
                  //判断是否输入框区域，是保留、不是清除
                  return if (event!!.x > left && event.x < right && event.y > top && event.y < bottom) {
                        false
                  } else {
                        view.apply {
                              isFocusable = false
                              isFocusableInTouchMode = true
                        }
                        true
                  }
            }
            return false
      }
}