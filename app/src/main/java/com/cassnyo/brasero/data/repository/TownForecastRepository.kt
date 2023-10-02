package com.cassnyo.brasero.data.repository

import com.cassnyo.brasero.data.database.BraseroDatabase
import com.cassnyo.brasero.data.database.entity.*
import com.cassnyo.brasero.data.database.join.TownForecast
import com.cassnyo.brasero.data.network.AemetApi
import com.cassnyo.brasero.data.network.response.common.ForecastApi
import com.cassnyo.brasero.data.network.response.daily.DailyForecastItemDayApi
import com.cassnyo.brasero.data.network.response.hourly.HourlyForecastItemDayApi
import com.cassnyo.brasero.di.module.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

class TownForecastRepository @Inject constructor(
    private val aemetApi: AemetApi,
    private val braseroDatabase: BraseroDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    fun observeTownForecast(townId: String): Flow<TownForecast> {
        return braseroDatabase.townForecastDao()
            .getTownForecastById(townId)
            .flowOn(ioDispatcher)
    }

    suspend fun refreshTownForecast(
        townId: String,
        forceRefresh: Boolean = false
    ) = withContext(ioDispatcher) {
        val town = braseroDatabase.townDao().getTown(townId)
        if (town == null || town.needsToUpdate() || forceRefresh) {
            val dailyForecast = getRemoteDailyForecastByTown(townId).toDailyEntities(townId)
            val hourlyForecast = getRemoteHourlyForecastByTown(townId).toHourlyEntities(townId)

            with(braseroDatabase) {
                runInTransaction {
                    dayForecastDao().deleteDailyForecastByTownId(townId)
                    dayForecastDao().saveDailyForecast(dailyForecast)
                    hourForecastDao().deleteHourlyForecastByTownId(townId)
                    hourForecastDao().saveHourlyForecast(hourlyForecast)
                }
            }
        }
    }

    private fun Town.needsToUpdate(): Boolean {
        if (updatedAt == null) return true
        val now = LocalDateTime.now()
        return now.isAfter(updatedAt.plusHours(1))
    }

    private fun ForecastApi<DailyForecastItemDayApi>.toDailyEntities(townId: String): List<DayForecast> {
        return forecast.day.map { dayApi ->
            // Group sky status by its description (using description as key) and then get the most repeated status
            val skyStatus = dayApi.skyStatus.groupBy { it.value }.maxByOrNull { it.value.size }?.key.orEmpty()
            // Use highest chanceOfRain of the whole day
            val chanceOfRain = dayApi.chanceOfRain.maxOf { it.value }
            // Same as skyStatus. Group by speed/direction and get the most repeated value
            val windSpeed = dayApi.wind.groupBy { it.speed }.maxByOrNull { it.value.size }?.key ?: 0
            val windDirection = dayApi.wind.groupBy { it.direction }.maxByOrNull { it.value.size }?.key.orEmpty()

            DayForecast(
                townId = townId,
                date = dayApi.date.toLocalDate(),
                skyStatus = skyStatus,
                chanceOfRain = chanceOfRain,
                wind = Wind(
                    windSpeed = windSpeed,
                    windDirection = windDirection
                ),
                temperature = Temperature(
                    minTemperature = dayApi.temperature.minimum,
                    maxTemperature = dayApi.temperature.maximum
                ),
                humidity = Humidity(
                    minHumidity = dayApi.relativeHumidity.minimum,
                    maxHumidity = dayApi.relativeHumidity.maximum
                )
            )
        }
    }

    private fun ForecastApi<HourlyForecastItemDayApi>.toHourlyEntities(townId: String): List<HourForecast> {
        val hourForecastMap = mutableListOf<HourForecast>()

        forecast.day.forEach { day ->

            (0..24).forEach { hour ->
                val fixedHour = when {
                    hour < 10 -> "0$hour"
                    else -> hour.toString().take(2)
                }

                val skyStatus = day.skyStatus.firstOrNull { it.period == fixedHour }
                val chanceOfRain = day.chanceOfRain.firstOrNull { it.period?.take(2) == fixedHour }
                val rain = day.rain.firstOrNull { it.period == fixedHour }
                val temperature = day.temperature.firstOrNull { it.period == fixedHour }
                val wind = day.wind.firstOrNull { it.period == fixedHour }
                val humidity = day.relativeHumidity.firstOrNull { it.period == fixedHour }

                if (skyStatus != null ||
                    chanceOfRain != null ||
                    rain != null ||
                    temperature != null ||
                    wind != null ||
                    humidity != null
                ) {
                    val rainValue = try {
                        rain?.value?.toInt() ?: 0
                    } catch (e: NumberFormatException) {
                        0
                    }
                    val temperatureValue = try {
                        temperature?.value?.toInt() ?: 0
                    } catch (e: NumberFormatException) {
                        0
                    }
                    val forecast = HourForecast(
                        townId = townId,
                        date = day.date.withHour(hour),
                        skyStatus = skyStatus?.value.orEmpty(),
                        chanceOfRain = chanceOfRain?.value?.toInt() ?: 0,
                        rain = rainValue,
                        temperature = temperatureValue,
                        wind = Wind(
                            windSpeed = wind?.speed?.firstOrNull()?.toInt() ?: 0,
                            windDirection = wind?.direction?.firstOrNull().orEmpty()
                        ),
                        humidity = humidity?.value?.toInt() ?: 0
                    )

                    hourForecastMap.add(forecast)
                }
            }
        }

        return hourForecastMap
    }

    private suspend fun getRemoteDailyForecastByTown(townId: String): ForecastApi<DailyForecastItemDayApi> {
        val wrapper = aemetApi.getDailyForecastByTownWrapper(townId)
        return aemetApi.getDailyForecastByTown(wrapper.data).first()
    }

    private suspend fun getRemoteHourlyForecastByTown(townId: String): ForecastApi<HourlyForecastItemDayApi> {
        val wrapper = aemetApi.getHourlyForecastByTownWrapper(townId)
        return aemetApi.getHourlyForecastByTown(wrapper.data).first()
    }

}