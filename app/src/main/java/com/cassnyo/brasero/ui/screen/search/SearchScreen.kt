package com.cassnyo.brasero.ui.screen.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cassnyo.brasero.R
import com.cassnyo.brasero.ui.common.navigation.NavigationRoutes
import com.cassnyo.brasero.ui.model.City

@Composable
fun SearchScreen(navController: NavController) {
    val viewModel: SearchViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState(initial = SearchViewModel.UiState())

    Column(modifier = Modifier.fillMaxSize()) {
        IconButton(onClick = { navController.navigateUp() }) {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowLeft,
                contentDescription = "Volver atrás",
                modifier = Modifier
                    .padding(4.dp)
                    .size(36.dp)
            )
        }

        SearchBar(
            query = state.query,
            onQueryChanged = viewModel::onQueryChanged,
            onClearQueryClicked = viewModel::onClearQueryClicked
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                state.query.isEmpty() -> SearchPlaceholder()
                !state.isLoading && state.cities.isEmpty() -> NoResultsFound(state.query)
            }

            CityList(
                cities = state.cities,
                onCityClicked = { city ->
                    navController.navigate(NavigationRoutes.forecast(city.id))
                },
                onAddCityClicked = viewModel::onAddCityClicked
            )
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onClearQueryClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        placeholder = {
            Text(text = "Buscar municipio")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Buscar municipio"
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClearQueryClicked) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Limpiar búsqueda"
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            keyboardType = KeyboardType.Text
        ),
        singleLine = true,
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
fun SearchPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_search_placeholder),
            contentDescription = "Buscar municipio",
            modifier = modifier.width(240.dp),
        )
    }
}

@Composable
fun NoResultsFound(
    query: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_not_found),
            contentDescription = "Sin resultados",
            modifier = Modifier.width(240.dp)
        )
        Text(
            text = "No hemos podido encontrar \"$query\"",
            modifier = Modifier.padding(24.dp),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.SemiBold
        )
    }
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