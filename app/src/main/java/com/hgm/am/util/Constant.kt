package com.hgm.am.util

import android.Manifest

/**
 * @author：  HGM
 * @date：  2023-04-19 9:53
 */
class Constant {
      companion object {
            //权限类型
            val PERMISSIONS_STORAGE = arrayOf(
                  Manifest.permission.CAMERA,
                  Manifest.permission.READ_EXTERNAL_STORAGE,
                  Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

            //权限请求码
            const val REQUEST_PERMISSION_CODE: Int = 3

            //截取数据长度值
            const val LENGTH = 43

            //TAG 日志
            const val MY_TAG = "Asset"

            //接口前缀
            const val BASE_URL = "http://47.107.231.209:20221/"

            //超时时间
            const val TIME_OUT = 30L

            //相机
            const val REQUEST_CODE_CAMERA = 103

            //相册
            const val REQUEST_CODE_ALBUM = 102

      }

      //11812073800014


}