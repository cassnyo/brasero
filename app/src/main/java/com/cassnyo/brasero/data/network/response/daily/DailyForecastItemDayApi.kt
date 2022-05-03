package com.cassnyo.brasero.data.network.response.daily

import com.cassnyo.brasero.data.network.response.common.MaxMinApi
import com.cassnyo.brasero.data.network.response.common.SkyStatusApi
import com.cassnyo.brasero.data.network.response.common.ValuePeriodApi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class DailyForecastItemDayApi(
    @Json(name = "fecha") val date: LocalDateTime,
    @Json(name = "probPrecipitacion") val chanceOfRain: List<ValuePeriodApi<Int>>,
    // FIXME Add cotaNieveProv
    @Json(name = "estadoCielo") val skyStatus: List<SkyStatusApi>,
    @Json(name = "viento") val wind: List<DailyWindApi>,
    // FIXME Add rachaMax
    @Json(name = "temperatura") val temperature: MaxMinApi,
    @Json(name = "sensTermica") val windchill: MaxMinApi,
    @Json(name = "humedadRelativa") val relativeHumidity: MaxMinApi,
    @Json(name = "uvMax") val uvMax: Int?
)