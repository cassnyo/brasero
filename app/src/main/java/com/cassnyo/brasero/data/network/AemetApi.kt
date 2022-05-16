package com.cassnyo.brasero.data.network

import com.cassnyo.brasero.data.network.response.common.AemetSuccessApi
import com.cassnyo.brasero.data.network.response.common.MasterTownApi
import com.cassnyo.brasero.data.network.response.common.ForecastApi
import com.cassnyo.brasero.data.network.response.daily.DailyForecastItemDayApi
import com.cassnyo.brasero.data.network.response.hourly.HourlyForecastItemDayApi
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface AemetApi {

    @GET("maestro/municipios")
    suspend fun getMasterTowns(): List<MasterTownApi>

    @GET("prediccion/especifica/municipio/diaria/{townId}")
    suspend fun getDailyForecastByTownWrapper(@Path("townId") townId: String): AemetSuccessApi

    @GET
    suspend fun getDailyForecastByTown(@Url url: String): List<ForecastApi<DailyForecastItemDayApi>>

    @GET("prediccion/especifica/municipio/horaria/{townId}")
    suspend fun getHourlyForecastByTownWrapper(@Path("townId") townId: String): AemetSuccessApi

    @GET
    suspend fun getHourlyForecastByTown(@Url url: String): List<ForecastApi<HourlyForecastItemDayApi>>

}