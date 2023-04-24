package com.hgm.am.model

data class BaseResponse(
    val code: Int,
    val `data`: List<Any>,
    val msg: String
)