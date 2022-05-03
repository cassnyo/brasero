package com.cassnyo.brasero.data.network.response.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SkyStatusApi(
    @Json(name = "value") val value: String,
    @Json(name = "periodo") val period: String?,
    @Json(name = "descripcion") val description: String
)