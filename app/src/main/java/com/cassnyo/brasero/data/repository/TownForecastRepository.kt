package com.cassnyo.brasero.data.repository

import com.cassnyo.brasero.data.database.BraseroDatabase
import com.cassnyo.brasero.data.database.entity.*
import com.cassnyo.brasero.data.database.join.TownForecast
import com.cassnyo.brasero.data.network.AemetApi
import com.cassnyo.brasero.data.network.response.common.ForecastApi
import com.cassnyo.brasero.data.network.response.daily.DailyForecastItemDayApi
import com.cassnyo.brasero.data.network.response.hourly.HourlyForecastItemDayApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TownForecastRepository @Inject constructor(
    private val aemetApi: AemetApi,
    private val braseroDatabase: BraseroDatabase
) {

    fun getTownsForecast(): Flow<List<TownForecast>> {
        return braseroDatabase.townForecastDao().getTownsForecast()
    }

    fun getTownForecast(townId: String): Flow<TownForecast> {
        return braseroDatabase.townForecastDao().getTownForecastById(townId)
    }

    suspend fun refreshTownForecast(townId: String) {
        // TODO Observe Room query for a single town
        // TODO refresh daily forecast
        // TODO refresh hourly forecast
        // TODO execute in parallel
        val dailyForecast = getDailyForecastByTown(townId)
        val hourlyForecast = getHourlyForecastByTown(townId)

        val forecast = Town(
            id = townId,
            townName = dailyForecast.name,
            provinceName = dailyForecast.province,
            isFavorite = true
        )
        val dailyForecastEntities = mapDailyForecastToEntities(dailyForecast, townId)
        val hourlyForecastEntities = mapHourlyForecastToEntities(hourlyForecast, townId)

        with(braseroDatabase) {
            runInTransaction {
                townDao().deleteTown(townId)
                townDao().saveTown(forecast)
                dayForecastDao().saveDailyForecast(dailyForecastEntities)
                hourForecastDao().saveHourlyForecast(hourlyForecastEntities)
            }
        }
    }

    private fun mapDailyForecastToEntities(
        dailyForecast: ForecastApi<DailyForecastItemDayApi>,
        townId: String
    ): List<DayForecast> {
        return dailyForecast.forecast.day.map { dayApi ->
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

    private fun mapHourlyForecastToEntities(
        hourlyForecast: ForecastApi<HourlyForecastItemDayApi>,
        townId: String
    ): List<HourForecast> {
        val hourForecastMap = mutableListOf<HourForecast>()

        hourlyForecast.forecast.day.forEach { day ->

            (0 .. 24).forEach { hour ->
                val fixedHour = when {
                    hour < 10 -> "0$hour"
                    else -> hour.toString().take(2)
                }

                val skyStatus = day.skyStatus.firstOrNull { it.period == fixedHour }
                val chanceOfRain = day.chanceOfRain.firstOrNull { it.period?.take(2) == fixedHour}
                val rain = day.rain.firstOrNull { it.period == fixedHour }
                val temperature = day.temperature.firstOrNull { it.period == fixedHour }
                val wind = day.wind.firstOrNull { it.period == fixedHour }
                val humidity = day.relativeHumidity.firstOrNull { it.period == fixedHour }

                if (skyStatus != null ||
                    chanceOfRain != null ||
                    rain != null ||
                    temperature != null ||
                    wind != null ||
                    humidity != null) {
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

    private suspend fun getDailyForecastByTown(townId: String): ForecastApi<DailyForecastItemDayApi> {
        val wrapper = aemetApi.getDailyForecastByTownWrapper(townId)
        return aemetApi.getDailyForecastByTown(wrapper.data).first()
    }

    private suspend fun getHourlyForecastByTown(townId: String): ForecastApi<HourlyForecastItemDayApi> {
        val wrapper = aemetApi.getHourlyForecastByTownWrapper(townId)
        return aemetApi.getHourlyForecastByTown(wrapper.data).first()
    }

}