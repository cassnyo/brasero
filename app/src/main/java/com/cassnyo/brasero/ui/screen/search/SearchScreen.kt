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
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cassnyo.brasero.R
import com.cassnyo.brasero.data.database.entity.Town
import com.cassnyo.brasero.ui.common.component.BraseroAppBar
import com.cassnyo.brasero.ui.common.component.PrettyLoading
import com.cassnyo.brasero.ui.common.navigation.NavigationRoutes
import com.cassnyo.brasero.ui.theme.ColorPrimary
import com.cassnyo.brasero.ui.theme.ColorPrimaryVariant

@Composable
fun SearchScreen(navController: NavController) {
    val viewModel: SearchViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState(initial = SearchUiState())

    Column(modifier = Modifier.fillMaxSize()) {
        BraseroAppBar(
            title = { Text(stringResource(R.string.search_title)) },
            onBackClicked = { navController.navigateUp() }
        )

        SearchField(
            query = state.query,
            onQueryChanged = viewModel::onQueryChanged,
            onClearQueryClicked = viewModel::onClearQueryClicked
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (state.isRefreshingTowns) {
                RefreshTownsLoading(modifier = Modifier.align(Alignment.Center))
            }

            if (state.noResultsFound()) {
                NoResultsFound(query = state.query)
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
private fun SearchField(
    query: String,
    onQueryChanged: (String) -> Unit,
    onClearQueryClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 8.dp,
                top = 4.dp,
                end = 8.dp,
                bottom = 0.dp
            ),
        placeholder = { Text(stringResource(R.string.search_field_hint)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = stringResource(R.string.search_field_description)
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClearQueryClicked) {
                    Icon(
                        imageVector = Icons.Rounded.Cancel,
                        contentDescription = stringResource(R.string.search_field_clear_description)
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            keyboardType = KeyboardType.Text
        ),
        singleLine = true,
        shape = RoundedCornerShape(size = 8.dp),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
private fun NoResultsFound(
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
            contentDescription = stringResource(R.string.search_no_results_description),
            modifier = Modifier.width(240.dp)
        )
        Text(
            text = stringResource(R.string.search_no_results, query),
            modifier = Modifier.padding(24.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun TownsList(
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
            key = { it.id }
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
                        contentDescription = stringResource(R.string.search_favorite_town_description)
                    )
                } else {
                    IconButton(onClick = { onAddTownClicked(town) }) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = stringResource(R.string.search_add_town_to_favorites_description)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RefreshTownsLoading(
    modifier: Modifier = Modifier
) {
    PrettyLoading(
        modifier = modifier,
        message = stringResource(R.string.search_loading_towns_description),
        size = 80.dp
    )
}