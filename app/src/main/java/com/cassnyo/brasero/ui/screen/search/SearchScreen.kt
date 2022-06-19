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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cassnyo.brasero.R
import com.cassnyo.brasero.data.database.entity.Town
import com.cassnyo.brasero.ui.common.component.PrettyLoading
import com.cassnyo.brasero.ui.common.navigation.NavigationRoutes

@Composable
fun SearchScreen(navController: NavController) {
    val viewModel: SearchViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState(initial = SearchViewModel.UiState())

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            query = state.query,
            onBackClicked = { navController.navigateUp() },
            onQueryChanged = viewModel::onQueryChanged,
            onClearQueryClicked = viewModel::onClearQueryClicked
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                state.isRefreshingTowns -> RefreshTownsLoading(modifier = Modifier.align(Alignment.Center))
                !state.isLoading && state.query.isNotEmpty() && state.towns.isEmpty() -> NoResultsFound(state.query)
            }

            TownsList(
                towns = state.towns,
                onTownClicked = { town ->
                    navController.navigate(NavigationRoutes.forecast(town.id))
                },
                onAddTownClicked = viewModel::onAddTownClicked
            )
        }
    }
}

@Composable
fun TopBar(
    query: String,
    onBackClicked: () -> Unit,
    onQueryChanged: (String) -> Unit,
    onClearQueryClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = 4.dp,
                end = 4.dp,
                bottom = 4.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClicked) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = "Volver atrás"
            )
        }

        SearchField(
            query = query,
            onQueryChanged = onQueryChanged,
            onClearQueryClicked = onClearQueryClicked,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
    }
}

@Composable
fun SearchField(
    query: String,
    onQueryChanged: (String) -> Unit,
    onClearQueryClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = modifier.focusRequester(focusRequester),
        placeholder = {
            Text(text = "Buscar municipio")
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClearQueryClicked) {
                    Icon(
                        imageVector = Icons.Rounded.Cancel,
                        contentDescription = "Limpiar búsqueda"
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Buscar municipio"
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            keyboardType = KeyboardType.Text
        ),
        singleLine = true,
        shape = CircleShape
    )
    
    LaunchedEffect(Unit) {
        // Request focus to open the keyboard
        focusRequester.requestFocus()
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
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun TownsList(
    towns: List<Town>,
    onTownClicked: (Town) -> Unit,
    onAddTownClicked: (Town) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        modifier = modifier
    ) {
        items(
            items = towns,
        ) { town ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(size = 2.dp))
                    .clickable { onTownClicked(town) }
                    .padding(start = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = town.townName)
                if (town.isFavorite) {
                    Icon(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(12.dp), // Same size/padding as an IconButton
                        imageVector = Icons.Rounded.TaskAlt,
                        contentDescription = "Municipio favorito"
                    )
                } else {
                    IconButton(onClick = { onAddTownClicked(town) }) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Añadir municipio"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RefreshTownsLoading(
    modifier: Modifier = Modifier
) {
    PrettyLoading(
        modifier = modifier,
        message = "Obteniendo ciudades disponibles",
        size = 80.dp
    )
}