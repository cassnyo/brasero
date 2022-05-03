package com.cassnyo.brasero.ui.screen.forecast

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cassnyo.brasero.R
import com.cassnyo.brasero.data.database.entity.DayForecast
import com.cassnyo.brasero.data.database.entity.HourForecast
import com.cassnyo.brasero.data.database.entity.Town
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ForecastScreen() {
    val viewModel: ForecastViewModel = hiltViewModel()

    val forecast = viewModel.forecast.collectAsState(initial = null).value

    if (forecast != null) {
        Column(Modifier.fillMaxSize()) {
            Header(
                town = forecast.town,
                selectedHourForecast = forecast.hours.first()
            )
            TodayForecast(hourForecastList = forecast.hours)
            WeekForecast(dayForecastList = forecast.days)
        }
    }
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    town: Town,
    selectedHourForecast: HourForecast
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            Text(text = "${town.townName}, ${town.provinceName}")
            Text(
                text = stringResource(R.string.forecast_temperature_celsius, selectedHourForecast.temperature),
                style = MaterialTheme.typography.h1,
                fontWeight = FontWeight.Bold
            )
            Text(text = selectedHourForecast.skyStatus)
        }
        Column {
            SkyStatusImage(
                modifier = Modifier.size(96.dp),
                skyStatus = selectedHourForecast.skyStatus
            )
        }
    }
}

@Composable
fun TodayForecast(
    modifier: Modifier = Modifier,
    hourForecastList: List<HourForecast>
) {
    Column(
        modifier = modifier.padding(
            horizontal = 16.dp,
            vertical = 8.dp
        )
    ) {
        Text(
            text = stringResource(R.string.forecast_today_title),
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.ExtraBold
        )
        LazyRow {
            items(hourForecastList) { hourForecast ->
                HourForecastItem(hourForecast = hourForecast)
            }
        }
    }
}

@Composable
fun HourForecastItem(
    modifier: Modifier = Modifier,
    hourForecast: HourForecast
) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = hourForecast.date.format(DateTimeFormatter.ofPattern("HH:mm")))
        SkyStatusImage(
            modifier = Modifier.size(24.dp),
            skyStatus = hourForecast.skyStatus
        )
        Text(text = stringResource(R.string.forecast_temperature_celsius, hourForecast.temperature))
    }
}

@Composable
fun WeekForecast(
    modifier: Modifier = Modifier,
    dayForecastList: List<DayForecast>
) {
    Text(
        modifier = Modifier.padding(
            horizontal = 16.dp,
            vertical = 8.dp
        ),
        text = stringResource(R.string.forecast_week_title),
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.ExtraBold
    )
    LazyColumn(modifier = modifier) {
        items(dayForecastList) { dayForecast ->
            DayForecastItem(dayForecast = dayForecast)
        }
    }
}

@Composable
fun DayForecastItem(
    modifier: Modifier = Modifier,
    dayForecast: DayForecast
) {
    Card(
        modifier = modifier
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = dayForecast.date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SkyStatusImage(
                    modifier = Modifier.size(24.dp),
                    skyStatus = dayForecast.skyStatus
                )
                Text(text = dayForecast.skyStatus)
            }

            Row {
                val temperature = dayForecast.temperature
                Text(
                    text = stringResource(R.string.forecast_temperature_celsius, temperature.maxTemperature),
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = stringResource(R.string.forecast_temperature_celsius, temperature.minTemperature),
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
    }
}

@Composable
private fun SkyStatusImage(
    modifier: Modifier = Modifier,
    skyStatus: String
) {
    val skyIcon = getSkyStatusIcon(skyStatus)
    if (skyIcon != null) {
        Image(
            painter = painterResource(id = skyIcon),
            contentDescription = skyStatus,
            modifier = modifier,
        )
    }
}

private fun getSkyStatusIcon(skyStatus: String): Int? {
    /*
    11 - Despejado
    12 - Poco nuboso
    13 -
    14 - Nuboso
    15 - Muy nuboso
    16 - Cubierto
    17 - Nubes altas
     */
    return when (skyStatus.removeSuffix("n")) {
        "11" -> R.drawable.ic_sunny
        "12" -> R.drawable.ic_sligthly_cloudy
        "14", "15", "16", "17" -> R.drawable.ic_cloudy
        else -> null
    }
}