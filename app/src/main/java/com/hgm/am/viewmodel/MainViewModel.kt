package com.hgm.am.viewmodel

import android.provider.MediaStore.Audio.Media
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hgm.am.model.AssetResponse
import com.hgm.am.http.HttpService
import com.hgm.am.http.RetrofitManager
import com.hgm.am.model.BaseResponse
import com.hgm.am.util.Constant.Companion.MY_TAG
import com.hgm.am.util.State
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

/**
 * @author：  HGM
 * @date：  2023-04-21 10:25
 */
class MainViewModel : ViewModel() {

      val assetInfo: MutableLiveData<State<AssetResponse>> = MutableLiveData()
      var assetResponse: AssetResponse? = null
      val upLoadInfo: MutableLiveData<State<BaseResponse>> = MutableLiveData()
      var upLoadResponse: BaseResponse? = null


      /**
       * 获取资产信息
       */
      fun getAssetData(number: String) = viewModelScope.launch {
            assetInfo.postValue(State.Loading())
            val response = RetrofitManager.create(HttpService::class.java).getAssetData(number)
            assetInfo.postValue(handleAssetResponse(response))
      }

      /**
       * 处理资产响应的Response
       */
      private fun handleAssetResponse(response: Response<AssetResponse>): State<AssetResponse> {
            response.body()?.let {
                  if (it.code == 200) {
                        assetResponse = it
                        Log.e(MY_TAG, "getAssetData:$it")
                        return State.Success(assetResponse)
                  } else {
                        return State.Error(response.body()?.msg ?: "")
                  }
            }
            return State.Error(response.body()?.msg ?: "")
      }


      /**
       * 上传图片
       */
      fun upLoadImg(number: String, file: File) = viewModelScope.launch {
            upLoadInfo.postValue(State.Loading())
            //1.创建MultipartBody.Builder对象
            val builder = MultipartBody.Builder()
                  .setType(MultipartBody.FORM)
            //2.设置表单类型
            val body = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            //3.添加表单数据
            builder.addFormDataPart("number", number)
            builder.addFormDataPart("img", file.name, body)
            val parts = builder.build().parts
            //4.获取响应体
            val response =
                  RetrofitManager.create(HttpService::class.java).uploadImage(parts)
           upLoadInfo.postValue(handleUpLoadResponse(response))
      }


      /**
       * 处理资产响应的Response
       */
      private fun handleUpLoadResponse(response: Response<BaseResponse>): State<BaseResponse> {
            response.body()?.let {
                  if (it.code == 200) {
                        upLoadResponse = it
                        Log.e(MY_TAG, "upLoadImg:$it")
                        return State.Success()
                  } else {
                        return State.Error(response.body()?.msg ?: "")
                  }
            }
            return State.Error(response.body()?.msg ?: "")
      }

}