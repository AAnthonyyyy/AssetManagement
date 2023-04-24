package com.hgm.am.util

/**
 * @author：  HGM
 * @date：  2023-04-21 14:46
 */
sealed class State<T>(
      val data: T? = null,
      val message: String? = null
) {
      class Success<T>(data: T?=null) : State<T>(data)
      class Error<T>(message: String, data: T?=null) : State<T>(data, message)
      class Loading<T> : State<T>()
}
