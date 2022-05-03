package com.cassnyo.brasero.data.network.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LocalTimeAdapter {

    @ToJson
    fun toJson(localTime: LocalTime): String {
        return localTime.format(DateTimeFormatter.ISO_LOCAL_TIME)
    }

    @FromJson
    fun fromJson(localTime: String): LocalTime {
        return LocalTime.parse(localTime, DateTimeFormatter.ISO_LOCAL_TIME)
    }

}