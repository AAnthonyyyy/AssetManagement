package com.hgm.am.http

import com.hgm.am.model.AssetResponse
import com.hgm.am.model.BaseResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query
import java.io.File

/**
 * @author：  HGM
 * @date：  2023-04-19 10:42
 */
interface HttpService {

      //通过扫码上传数据，更新最新的数据
      @PUT("v1/checkassets")
      fun uploadData(@Query("number") number: String): Call<ResponseBody>

      //通过资产设备编号查询具体数据
      @GET("v1/getAssets")
      suspend fun getAssetData(@Query("assets_number") number: String):Response<AssetResponse>

      //上传资产设备图片，盘点
      @Multipart
      @POST("v1/checkassets/photo")
      suspend fun uploadImage(
            @Part list:List<MultipartBody.Part>
      ): Response<BaseResponse>
}