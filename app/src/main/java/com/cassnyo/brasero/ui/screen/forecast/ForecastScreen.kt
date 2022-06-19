package com.cassnyo.brasero.ui.screen.forecast

import android.content.Context
import android.content.res.Resources
import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cassnyo.brasero.R
import com.cassnyo.brasero.data.database.entity.DayForecast
import com.cassnyo.brasero.data.database.entity.HourForecast
import com.cassnyo.brasero.data.database.entity.Town
import com.cassnyo.brasero.data.database.join.TownForecast
import com.cassnyo.brasero.ui.theme.ColorOnPrimary
import com.cassnyo.brasero.ui.theme.ColorOnSurface
import com.cassnyo.brasero.ui.theme.ColorPrimary
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ForecastScreen(navController: NavController) {
    val viewModel: ForecastViewModel = hiltViewModel()

    val forecast = viewModel.townForecast.collectAsState(initial = null).value

    if (forecast != null) {
        TownForecast(forecast)
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
               town = forecast.town,
               selectedHourForecast = selectedHourForecast
           )
           TodayForecast(
               hourForecastList = forecast.hours,
               selectedHourForecast = selectedHourForecast,
               onHourForecastClicked = { selectedHourForecast = it }
           )
           WeekForecast(
               modifier = Modifier.weight(1f, false),
               dayForecastList = forecast.days
           )
       }
   }
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    town: Town,
    selectedHourForecast: HourForecast
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${town.townName}, ${town.provinceName}",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        SkyStatusImage(
            modifier = Modifier.size(96.dp),
            skyStatus = selectedHourForecast.skyStatus
        )
        Text(text = selectedHourForecast.skyStatus)

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.forecast_temperature_celsius, selectedHourForecast.temperature),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun TodayForecast(
    hourForecastList: List<HourForecast>,
    selectedHourForecast: HourForecast,
    onHourForecastClicked: (HourForecast) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(
            horizontal = 16.dp,
            vertical = 8.dp
        )
    ) {
        Text(
            text = stringResource(R.string.forecast_next_24_hours_title),
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.ExtraBold
        )
        LazyRow(
            contentPadding = PaddingValues(
                vertical = 4.dp,
                horizontal = 4.dp
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
fun HourForecastItem(
    hourForecast: HourForecast,
    selectedHourForecast: HourForecast,
    onHourForecastClicked: (HourForecast) -> Unit,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor) = when (hourForecast) {
        selectedHourForecast -> Pair(ColorPrimary.copy(alpha = 0.4f), ColorOnPrimary)
        else -> Pair(Color.Transparent, ColorOnSurface)
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
            modifier = Modifier.size(48.dp),
            skyStatus = hourForecast.skyStatus
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
fun WeekForecast(
    modifier: Modifier = Modifier,
    dayForecastList: List<DayForecast>
) {
    Text(
        modifier = Modifier.padding(
            horizontal = 16.dp,
            vertical = 8.dp
        ),
        text = stringResource(R.string.forecast_7_days_title),
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.ExtraBold
    )
    dayForecastList.forEach { dayForecast ->
        DayForecastItem(dayForecast = dayForecast)
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
    val skyIcon = getSkyStatusIcon(LocalContext.current, skyStatus)
    if (skyIcon != null) {
        Image(
            painter = painterResource(id = skyIcon),
            contentDescription = skyStatus,
            modifier = modifier,
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
        Log.w("Forecast", "Resource not found: $identifier")
    }
}