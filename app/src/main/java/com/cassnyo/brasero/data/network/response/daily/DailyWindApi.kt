package com.cassnyo.brasero.data.network.response.daily

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DailyWindApi(
    @Json(name = "direccion") val direction: String,
    @Json(name = "velocidad") val speed: Int,
    @Json(name = "periodo") val period: String?
)