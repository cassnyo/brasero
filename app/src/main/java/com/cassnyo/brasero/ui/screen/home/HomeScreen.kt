package com.cassnyo.brasero.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AddLocationAlt
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cassnyo.brasero.ui.common.navigation.NavigationRoutes
import com.cassnyo.brasero.ui.theme.ColorPrimary
import com.cassnyo.brasero.ui.theme.ColorPrimaryVariant
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel : HomeViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState(initial = HomeViewModel.UiState())

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val pagerState = rememberPagerState()

        Header(
            pagerState = pagerState,
            onSearchClicked = {
                navController.navigate(NavigationRoutes.SEARCH)
            },
            onDotsClicked = {
                // TODO
            }
        )

        val favoriteTownsCount = state.favoriteTowns.size
        if (favoriteTownsCount == 0) {
            AddYourFirstTown()
        } else {
            HorizontalPager(
                count = favoriteTownsCount,
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                // FIXME Refactor TownForecast to receive a favorite town's id instead of the forecast
                // TownForecast(forecast = state.favoriteTowns[page])
            }
        }
    }
}

@Composable
private fun Header(
    pagerState: PagerState,
    onSearchClicked: () -> Unit,
    onDotsClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onSearchClicked) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Buscar municipio"
            )
        }

        if (pagerState.pageCount > 0) {
            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(bottom = 16.dp),
                indicatorWidth = 4.dp
            )
        }

        IconButton(onClick = onDotsClicked) {
            Icon(
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = "Menú extendido"
            )
        }
    }
}

@Composable
private fun AddYourFirstTown(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.AddLocationAlt,
            contentDescription = "Añade tu primer municipio",
            modifier = Modifier.size(120.dp),
            tint = ColorPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Añade tu primer municipio",
            modifier = Modifier.padding(horizontal = 24.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.SemiBold
        )

        AddYourFirstTownMessage()
    }
}

@Composable
private fun AddYourFirstTownMessage(
    modifier: Modifier = Modifier
) {
    val searchId = "search"
    val addId = "add"
    val text = buildAnnotatedString {
        append("Aún no has añadido ningún municipio a favoritos. Pulsa en ")
        appendInlineContent(searchId, "[icon]")
        append(" y añádelo pulsando en ")
        appendInlineContent(addId, "[icon]")
    }
    val inlineContent = mapOf(
        searchId to buildInlineIconText(Icons.Rounded.Search, "Buscar municipio"),
        addId to buildInlineIconText(Icons.Rounded.Add, "Añadir municipio")
    )
    Text(
        text = text,
        inlineContent = inlineContent,
        modifier = Modifier.padding(horizontal = 24.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.subtitle1
    )
}

private fun buildInlineIconText(
    imageVector: ImageVector,
    contentDescription: String?
): InlineTextContent {
    return InlineTextContent(
        Placeholder(
            width = 20.sp,
            height = 20.sp,
            placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
        )
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = ColorPrimaryVariant
        )
    }
}