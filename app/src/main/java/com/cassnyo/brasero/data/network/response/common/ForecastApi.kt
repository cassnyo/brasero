package com.cassnyo.brasero.data.network.response.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class ForecastApi<T>(
    @Json(name = "elaborado") val date: LocalDateTime,
    @Json(name = "nombre") val name: String,
    @Json(name = "provincia") val province: String,
    @Json(name = "prediccion") val forecast: ForecastItemApi<T>
)