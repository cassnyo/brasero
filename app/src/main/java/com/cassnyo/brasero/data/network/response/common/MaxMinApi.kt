package com.cassnyo.brasero.data.network.response.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MaxMinApi(
    @Json(name = "maxima") val maximum: Int,
    @Json(name = "minima") val minimum: Int,
    @Json(name = "dato") val data: List<ValueHourApi>
)