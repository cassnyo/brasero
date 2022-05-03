package com.cassnyo.brasero.data.network.response.hourly

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HourlyWindApi(
    @Json(name = "direccion") val direction: List<String>?,
    @Json(name = "velocidad") val speed: List<String>?,
    @Json(name = "periodo") val period: String
)