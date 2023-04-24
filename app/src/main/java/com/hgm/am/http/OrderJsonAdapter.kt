package com.hgm.am.http

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.hgm.am.model.AssetResponse
import okio.IOException

/**
 * @author：  HGM
 * @date：  2023-04-23 9:30
 */
class OrderJsonAdapter : TypeAdapter<AssetResponse?>() {
      @Throws(IOException::class)
      override fun write(out: JsonWriter?, value: AssetResponse?) {
            //用不到
      }

      @Throws(IOException::class)
      override fun read(`in`: JsonReader): AssetResponse? {
            if (`in`.peek() == JsonToken.NULL) { //由于服务端在没有数据的时候返回的是[]，我们要跳过这个情况
                  `in`.nextNull()
                  return null
            }
            return Gson().fromJson(`in`, AssetResponse::class.java)
      }
}