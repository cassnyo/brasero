package com.cassnyo.brasero.data.network.response.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ValueHourApi(
    @Json(name = "value") val value: Int,
    @Json(name = "hora") val hour: Int
)