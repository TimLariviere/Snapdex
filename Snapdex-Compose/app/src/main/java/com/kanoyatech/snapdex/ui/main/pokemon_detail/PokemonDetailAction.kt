package com.kanoyatech.snapdex.ui.main.pokemon_detail

import com.kanoyatech.snapdex.domain.models.PokemonId

sealed interface PokemonDetailAction {
    data object OnFavoriteToggleClick: PokemonDetailAction
    data object OnBackClick: PokemonDetailAction
    data class OnPokemonClick(val pokemonId: PokemonId): PokemonDetailAction
}