package com.cassnyo.brasero.ui.screen.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cassnyo.brasero.ui.model.City

@Composable
fun SearchScreen() {
    val viewModel: SearchViewModel = hiltViewModel()

    val state = viewModel.state.collectAsState(initial = null).value

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = state?.query.orEmpty(),
            onQueryChanged = viewModel::onQueryChanged
        )

        CityList(
            cities = state?.cities.orEmpty(),
            onCityClicked = viewModel::onCityClicked,
            onAddCityClicked = viewModel::onAddCityClicked
        )
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        placeholder = {
            Text(text = "Buscar municipio")
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Buscar municipio"
            )
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            keyboardType = KeyboardType.Text
        ),
        singleLine = true
    )
}

@Composable
fun CityList(
    cities: List<City>,
    onCityClicked: (City) -> Unit,
    onAddCityClicked: (City) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp)
    ) {
        items(
            items = cities,
        ) { city ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(size = 2.dp))
                    .clickable { onCityClicked(city) }
                    .padding(start = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = city.name)
                IconButton(onClick = { onAddCityClicked(city) }) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Añadir ciudad"
                    )
                }
            }
        }
    }
}