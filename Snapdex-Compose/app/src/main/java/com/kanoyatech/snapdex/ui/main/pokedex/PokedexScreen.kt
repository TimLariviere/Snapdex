package com.kanoyatech.snapdex.ui.main.pokedex

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kanoyatech.snapdex.R
import com.kanoyatech.snapdex.domain.Pokemon
import com.kanoyatech.snapdex.domain.PokemonId
import com.kanoyatech.snapdex.theme.AppTheme
import com.kanoyatech.snapdex.theme.designsystem.SnapdexSearchField
import com.kanoyatech.snapdex.theme.snapdexRed200
import com.kanoyatech.snapdex.ui.State
import com.kanoyatech.snapdex.ui.TypeUi
import com.kanoyatech.snapdex.ui.utils.mediumImageId
import com.kanoyatech.snapdex.ui.main.pokedex.components.SmallTypeBadge
import com.kanoyatech.snapdex.ui.utils.getLocale
import org.koin.androidx.compose.koinViewModel

@Composable
fun PokedexScreenRoot(
    paddingValues: PaddingValues,
    viewModel: PokedexViewModel = koinViewModel(),
    onPokemonClick: (PokemonId) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(true) {
        val locale = context.getLocale()
        viewModel.setLocale(locale)
    }

    PokedexScreen(
        paddingValues = paddingValues,
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is PokedexAction.OnPokemonClick -> onPokemonClick(action.pokemonId)
            }

            viewModel.onAction(action)
        }
    )
}

@Composable
private fun PokedexScreen(
    paddingValues: PaddingValues,
    state: PokedexState,
    onAction: (PokedexAction) -> Unit
) {
    when (state.state) {
        State.LOADING ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(64.dp)
                )
            }
        State.IDLE -> PokemonGrid(
            paddingValues = paddingValues,
            state = state,
            onAction = onAction
        )
    }
}

@Composable
fun PokemonGrid(
    paddingValues: PaddingValues,
    state: PokedexState,
    onAction: (PokedexAction) -> Unit
) {
    Box {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFD8E6F8),
                            Color(0xFFA9CEFC)
                        )
                    )
                )
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 24.dp)
        ) {
            SnapdexSearchField(
                state = state.searchText,
                hint = stringResource(id = R.string.search_hint),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .weight(1f)
            ) {
                items(state.pokemons, key = { it.id }) { pokemon ->
                    PokemonItem(
                        pokemon = pokemon,
                        modifier = Modifier
                            .clickable {
                                onAction(PokedexAction.OnPokemonClick(pokemon.id))
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonItem(
    pokemon: Pokemon,
    modifier: Modifier = Modifier
) {
    val types = pokemon.types.map { TypeUi.fromType(it) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .height(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.40f))
            .padding(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            types.forEach { type ->
                SmallTypeBadge(type)
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                bitmap = ImageBitmap.imageResource(id = pokemon.mediumImageId),
                contentDescription = pokemon.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Text(
                text = stringResource(id = R.string.pokemon_number, pokemon.id),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun UnknownItem(
    id: PokemonId,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = "?",
                fontSize = 32.sp
            )
        }

        Text(
            text = stringResource(id = R.string.pokemon_number, id)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PokedexScreenPreview() {
    AppTheme {
        PokedexScreen(
            paddingValues = PaddingValues(0.dp),
            state = PokedexState(
                state = State.IDLE
            ),
            onAction = {}
        )
    }
}