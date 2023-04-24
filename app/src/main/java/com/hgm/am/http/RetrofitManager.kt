package com.hgm.am.http

import com.google.gson.GsonBuilder
import com.hgm.am.model.AssetResponse
import com.hgm.am.util.Constant.Companion.BASE_URL
import com.hgm.am.util.Constant.Companion.TIME_OUT
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author：  HGM
 * @date：  2023-04-19 10:46
 */
object RetrofitManager {

      private val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .build()


      private val gson = GsonBuilder()
            .registerTypeAdapter(AssetResponse::class.java, OrderJsonAdapter())
            .create()

      private val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

      fun <T> create(clazz: Class<T>): T {
            return retrofit.create(clazz)
      }
}