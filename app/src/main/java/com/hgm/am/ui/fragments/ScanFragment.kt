package com.hgm.am.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.hgm.am.databinding.FragmentScanBinding
import com.hgm.am.http.HttpService
import com.hgm.am.http.RetrofitManager
import com.hgm.am.util.Constant.Companion.LENGTH
import com.hgm.am.util.Constant.Companion.MY_TAG
import com.king.zxing.CameraScan.SCAN_RESULT
import com.king.zxing.CaptureActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class ScanFragment : Fragment() {

      private var _binding: FragmentScanBinding? = null
      private val binding get() = _binding!!


      override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
      ): View {
            _binding = FragmentScanBinding.inflate(inflater, container, false)
            return binding.root
      }

      override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            //registerForActivityResult只能在on..Create()中注册，并且不能放在点击监听方法里
            val resultLauncher =
                  registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                        //判断 resultCode
                        if (it.resultCode == RESULT_OK) {
                              val result = it.data?.getStringExtra(SCAN_RESULT)
                              val tag = result?.substring(7,11)
                              Log.e(MY_TAG, "--------------: $tag", )
                              // if (tag=="zjgk"){
                              //       val num = result?.substring(LENGTH)
                              //       Log.e(MY_TAG, "扫描二维码出来的数据: $result   截取的数据: $num")
                              //       //拿到扫描的数据后开始请求网络
                              //       uploadData(num.toString())
                              if(result.contains("zjgk")){
                                    val num = result?.substring(LENGTH)
                                    Log.e(MY_TAG, "扫描二维码出来的数据: $result   截取的数据: $num")
                                    //拿到扫描的数据后开始请求网络
                                    uploadData(num.toString())
                              }
                              }else{
                                    Toast.makeText(activity, "请扫描正确的二维码", Toast.LENGTH_SHORT).show()
                              }

                        }
                  }


            binding.btnScan.setOnClickListener {
                  Intent(activity, CaptureActivity::class.java).let {
                        resultLauncher.launch(it)
                  }
            }
      }

      /**
       * 扫码更新数据
       */
      private fun uploadData(number: String) {
            RetrofitManager.create(HttpService::class.java).uploadData(number)
                  .enqueue(object : retrofit2.Callback<ResponseBody> {
                        override fun onResponse(
                              call: Call<ResponseBody>,
                              response: Response<ResponseBody>
                        ) {
                              if (response.code()==200){
                                    Toast.makeText(activity, "扫描成功", Toast.LENGTH_SHORT).show()
                                    Log.e(MY_TAG, "网络请求状态: " + response.code())
                              }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                              Toast.makeText(activity, "扫描失败", Toast.LENGTH_SHORT).show()
                              Log.e(MY_TAG, "网络请求失败信息: ${t.message}")
                        }
                  })
      }


      override fun onDestroy() {
            super.onDestroy()
            _binding = null
      }


}
