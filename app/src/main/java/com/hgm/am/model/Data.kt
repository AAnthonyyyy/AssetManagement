package com.hgm.am.model

data class Data(
    val acquisition_time: String?,
    val assets_number: String?,
    val check_time: String?,
    val company: String?,
    val department: String?,
    val device_status: String?,
    val id: Int?,
    val is_delete: String?,
    val is_print: String?,
    val model: String?,
    val name: String?,
    val position: String?,
    val price: String?,
    val purpose: String?,
    val remarks: String?,
    val scrap_time: Int?,
    val service_life: String?,
    val update_year: String?,
    val use_status: String?,
    val user: User?,
    val user_id: Int?
)