package com.cassnyo.brasero.ui.screen.forecast

import android.content.Context
import android.content.res.Resources
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cassnyo.brasero.R
import com.cassnyo.brasero.common.extension.isToday
import com.cassnyo.brasero.common.extension.isTomorrow
import com.cassnyo.brasero.data.database.entity.DayForecast
import com.cassnyo.brasero.data.database.entity.HourForecast
import com.cassnyo.brasero.data.database.entity.Humidity
import com.cassnyo.brasero.data.database.entity.Temperature
import com.cassnyo.brasero.data.database.entity.Wind
import com.cassnyo.brasero.data.database.join.TownForecast
import com.cassnyo.brasero.ui.common.component.BraseroAppBar
import com.cassnyo.brasero.ui.theme.BraseroTheme
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ForecastScreen(navController: NavController) {
    val viewModel: ForecastViewModel = hiltViewModel()

    val forecast = viewModel.townForecast.collectAsState(initial = null).value

    if (forecast != null) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            BraseroAppBar(
                title = { Text(text = forecast.town.townName) },
                onBackClicked = { navController.navigateUp() }
            )
            TownForecast(forecast)
        }
    }
}

@Composable
fun TownForecast(
    forecast: TownForecast
) {
   if (forecast.hours.isNotEmpty()) {
       var selectedHourForecast by remember { mutableStateOf(forecast.hours.first()) }
       val scrollState = rememberScrollState()

       Column(modifier = Modifier
           .fillMaxSize()
           .verticalScroll(state = scrollState)
       ) {
           Header(
               selectedHourForecast = selectedHourForecast
           )
           Spacer(modifier = Modifier.height(16.dp))
           NextHoursForecast(
               hourForecastList = forecast.hours,
               selectedHourForecast = selectedHourForecast,
               onHourForecastClicked = { selectedHourForecast = it }
           )
           NextDaysForecast(
               nextDaysForecast = forecast.days
           )
       }
   }
}

@Composable
private fun Header(
    selectedHourForecast: HourForecast,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderTop(selectedHourForecast = selectedHourForecast)
        Spacer(modifier = Modifier.height(16.dp))
        HeaderBottom(selectedHourForecast = selectedHourForecast)
    }
}

@Composable
private fun HeaderBottom(
    selectedHourForecast: HourForecast,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Wind
        ColumnDetail(
            title = stringResource(R.string.forecast_wind),
            value = stringResource(R.string.forecast_wind_value, selectedHourForecast.wind.windSpeed)
        )

        // Humidity
        ColumnDetail(
            title = stringResource(R.string.forecast_humidity),
            value = stringResource(R.string.forecast_humidity_value, selectedHourForecast.humidity)
        )

        // Rain // chance of rain
        ColumnDetail(
            title = stringResource(R.string.forecast_rain),
            value = stringResource(R.string.forecast_rain_value, selectedHourForecast.chanceOfRain)
        )
    }
}

@Composable
private fun HeaderTop(
    selectedHourForecast: HourForecast,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val skyStatus = selectedHourForecast.skyStatus
        SkyStatusImage(
            skyStatus = skyStatus,
            modifier = Modifier.size(96.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.forecast_temperature_celsius, selectedHourForecast.temperature),
            style = MaterialTheme.typography.h2,
            fontWeight = FontWeight.SemiBold
        )
        SkyStatusDescription(skyStatus = skyStatus)
    }
}

@Composable
private fun NextHoursForecast(
    hourForecastList: List<HourForecast>,
    selectedHourForecast: HourForecast,
    onHourForecastClicked: (HourForecast) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.forecast_next_hours_title),
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
        )
        LazyRow(
            contentPadding = PaddingValues(
                vertical = 4.dp,
                horizontal = 16.dp
            )
        ) {
            items(
                items = hourForecastList,
                key = { it.date }
            ) { hourForecast ->
                HourForecastItem(
                    hourForecast = hourForecast,
                    selectedHourForecast = selectedHourForecast,
                    onHourForecastClicked = onHourForecastClicked
                )
            }
        }
    }
}

@Composable
private fun HourForecastItem(
    hourForecast: HourForecast,
    selectedHourForecast: HourForecast,
    onHourForecastClicked: (HourForecast) -> Unit,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor) = when (hourForecast) {
        selectedHourForecast -> Pair(MaterialTheme.colors.primary.copy(alpha = 0.4f), MaterialTheme.colors.onPrimary)
        else -> Pair(Color.Transparent, MaterialTheme.colors.onSurface)
    }
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(size = 4.dp))
            .clickable { onHourForecastClicked(hourForecast) }
            .background(backgroundColor)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = hourForecast.date.format(DateTimeFormatter.ofPattern("HH:mm")),
            color = textColor,
            style = MaterialTheme.typography.subtitle1
        )

        Spacer(modifier = Modifier.height(4.dp))
        SkyStatusImage(
            skyStatus = hourForecast.skyStatus,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.forecast_temperature_celsius, hourForecast.temperature),
            color = textColor,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun NextDaysForecast(
    nextDaysForecast: List<DayForecast>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.forecast_next_days_title),
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.ExtraBold
        )
        nextDaysForecast.forEach { nextDayForecast ->
            NextDayForecast(
                nextDayForecast = nextDayForecast,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun NextDayForecast(
    nextDayForecast: DayForecast,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Date
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.weight(1f)
        ) {
            val date = nextDayForecast.date
            val dayName = when {
                date.isToday() -> stringResource(R.string.forecast_next_days_today)
                date.isTomorrow() -> stringResource(R.string.forecast_next_days_tomorrow)
                else -> date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
            }
            Text(
                text = dayName,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = date.format(DateTimeFormatter.ofPattern("MMM, dd")),
                style = MaterialTheme.typography.caption
            )
        }

        // Sky
        SkyStatusImage(
            skyStatus = nextDayForecast.skyStatus,
            modifier = Modifier.size(42.dp)
        )

        // Temperature
        val temperature = nextDayForecast.temperature
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.weight(1f)
        ) {
            val temperatureString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.SemiBold
                    )
                ) {
                    append(stringResource(R.string.forecast_temperature_celsius, temperature.minTemperature))
                }
                append(" / ")
                withStyle(style = SpanStyle()) {
                    append(stringResource(R.string.forecast_temperature_celsius, temperature.maxTemperature))
                }
            }

            Text(
                text = temperatureString,
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
private fun SkyStatusImage(
    skyStatus: String,
    modifier: Modifier = Modifier
) {
    val skyIcon = getSkyStatusIcon(LocalContext.current, skyStatus)
    if (skyIcon != null) {
        Image(
            painter = painterResource(id = skyIcon),
            contentDescription = skyStatus,
            modifier = modifier,
        )
    }
}

@Composable
private fun SkyStatusDescription(
    skyStatus: String,
    modifier: Modifier = Modifier
) {
    val skyDescription = getSkyStatusDescription(LocalContext.current, skyStatus)
    Text(
        text = stringResource(id = skyDescription),
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun ColumnDetail(
    title: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.caption
        )
        Text(
            text = value,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

private fun getSkyStatusIcon(context: Context, skyStatus: String): Int? {
    /*
    11 - Despejado
    12 - Poco nuboso
    13 -
    14 - Nuboso
    15 - Muy nuboso
    16 - Cubierto
    17 - Nubes altas
     */
    if (skyStatus.isEmpty()) return null
    val identifier = "sky_$skyStatus"
    return try {
        context.resources.getIdentifier(identifier, "drawable", context.packageName)
    } catch (e: Resources.NotFoundException) {
        Timber.w("Sky icon not found: $identifier")
        null
    }
}

private fun getSkyStatusDescription(context: Context, skyStatus: String): Int {
    if (skyStatus.isEmpty()) return R.string.sky_not_found
    val identifier = "sky_${skyStatus.removeSuffix("n")}"
    return try {
        context.resources.getIdentifier(identifier, "string", context.packageName)
    } catch (e: Resources.NotFoundException) {
        Timber.w("Sky description not found: $identifier")
        R.string.sky_not_found
    }
}

@Preview(showBackground = true)
@Composable
private fun HeaderPreview() {
    BraseroTheme {
        Header(
            selectedHourForecast = HourForecast(
                id = null,
                townId = "",
                date = LocalDateTime.now(),
                skyStatus = "13n",
                chanceOfRain = 15,
                rain = 14,
                temperature = 24,
                wind = Wind(10, "NE"),
                humidity = 30
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NextDaysForecastPreview() {
    BraseroTheme {
        NextDaysForecast(
            nextDaysForecast = listOf(
                DayForecast(
                    id = null,
                    townId = "",
                    date = LocalDate.now(),
                    skyStatus = "13n",
                    chanceOfRain = 0,
                    temperature = Temperature(24, 30),
                    wind = Wind(10, "NE"),
                    humidity = Humidity(0, 80)
                )
            )
        )
    }
}