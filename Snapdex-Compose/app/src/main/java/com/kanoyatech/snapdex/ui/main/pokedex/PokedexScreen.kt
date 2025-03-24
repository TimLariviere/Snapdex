package com.kanoyatech.snapdex.ui.main.pokedex

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.kanoyatech.snapdex.R
import com.kanoyatech.snapdex.domain.models.Pokemon
import com.kanoyatech.snapdex.domain.models.PokemonId
import com.kanoyatech.snapdex.theme.AppTheme
import com.kanoyatech.snapdex.theme.Icons
import com.kanoyatech.snapdex.theme.SnapdexTheme
import com.kanoyatech.snapdex.theme.designsystem.SnapdexFloatingActionButton
import com.kanoyatech.snapdex.theme.designsystem.SnapdexText
import com.kanoyatech.snapdex.theme.designsystem.search.SnapdexSearchView
import com.kanoyatech.snapdex.ui.TypeUi
import com.kanoyatech.snapdex.ui.main.pokedex.components.PokemonCaughtOverlay
import com.kanoyatech.snapdex.ui.utils.mediumImageId
import com.kanoyatech.snapdex.ui.main.pokedex.components.SmallTypeBadge
import com.kanoyatech.snapdex.ui.utils.ObserveAsEvents
import com.kanoyatech.snapdex.ui.utils.getLocale
import com.kanoyatech.snapdex.ui.utils.translated
import org.koin.androidx.compose.koinViewModel

@Composable
fun PokedexScreenRoot(
    paddingValues: PaddingValues,
    viewModel: PokedexViewModel = koinViewModel(),
    onPokemonClick: (PokemonId) -> Unit,
    onPokemonCatch: (PokemonId) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(true) {
        viewModel.initialize(context)
    }

    val configuration = LocalConfiguration.current
    val locale = configuration.getLocale()
    LaunchedEffect(locale) {
        viewModel.locale = locale
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is PokedexEvent.OnPokemonCatch -> onPokemonCatch(event.pokemonId)
        }
    }

    PokedexScreen(
        paddingValues = paddingValues,
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is PokedexAction.OnPokemonClick -> onPokemonClick(action.pokemonId)
                else -> Unit
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
    Box {
        Column(
            modifier = Modifier
                .padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    top = paddingValues.calculateTopPadding(),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
                )
                .padding(top = 16.dp)
        ) {
            SnapdexSearchView(
                state = state.searchState,
                hint = stringResource(id = R.string.search_hint),
                onRemoveFilterClick = { type ->
                    onAction(PokedexAction.RemoveFilterClick(type))
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 88.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .weight(1f)
            ) {
                if (state.filteredPokemons != null) {
                    items(state.filteredPokemons, key = { it.id }) { pokemon ->
                        PokemonItem(
                            pokemon = pokemon,
                            modifier = Modifier
                                .clickable {
                                    onAction(PokedexAction.OnPokemonClick(pokemon.id))
                                }
                        )
                    }
                } else {
                    items(151, key = { it + 1 }) { index ->
                        val pokemonId = index + 1
                        val pokemon = state.allPokemons.find { it.id == pokemonId }
                        if (pokemon == null) {
                            UnknownItem(pokemonId)
                        } else {
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

        TakePictureButton(
            state = state,
            onAction = onAction,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = paddingValues.calculateBottomPadding() + 16.dp)
        )

        if (state.lastCaught != null) {
            PokemonCaughtOverlay(
                pokemon = state.lastCaught,
                onDismissRequest = {
                    onAction(PokedexAction.OnPokemonCaughtDialogDismiss)
                }
            )
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
            .clip(SnapdexTheme.shapes.regular)
            .background(SnapdexTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = SnapdexTheme.colorScheme.outline,
                shape = SnapdexTheme.shapes.regular
            )
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
                contentDescription = pokemon.name.translated(),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            SnapdexText(
                text = stringResource(id = R.string.pokemon_number_alt, pokemon.id)
            )
        }
    }
}

@Composable
fun UnknownItem(
    id: PokemonId,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(SnapdexTheme.shapes.regular)
            .background(SnapdexTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = SnapdexTheme.colorScheme.outline,
                shape = SnapdexTheme.shapes.regular
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        SnapdexText(
            text = stringResource(id = R.string.pokemon_number_alt, id)
        )
    }
}

@Composable
fun TakePictureButton(
    state: PokedexState,
    onAction: (PokedexAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = LocalActivity.current

    // On first composition, check if we already have the camera permission granted
    LaunchedEffect(true) {
        val isGranted = activity?.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        onAction(PokedexAction.IsCameraGrantedChange(isGranted))
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { image ->
            if (image != null) {
                onAction(PokedexAction.OnPhotoTake(image))
            }
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            onAction(PokedexAction.IsCameraGrantedChange(isGranted))

            // If the user just gave us the permission, immediately take a picture
            if (isGranted) {
                cameraLauncher.launch()
            }
        }

    SnapdexFloatingActionButton(
        onClick = {
            when {
                !(activity?.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ?: false) -> {
                    Log.i("TakePictureButton", "Camera permission is not granted yet. Asking permission")
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
                !state.isCameraGranted -> {
                    Log.i("TakePictureButton", "Camera permission is denied")
                    // Display error message
                }
                else -> {
                    Log.i("TakePictureButton", "Camera permission is granted. Taking picture")
                    cameraLauncher.launch()
                }
            }
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Pokeball,
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PokedexScreenPreview() {
    AppTheme {
        PokedexScreen(
            paddingValues = PaddingValues(0.dp),
            state = PokedexState(),
            onAction = {}
        )
    }
}