package com.kanoyatech.snapdex.data.repositories

import com.kanoyatech.snapdex.data.local.dao.EvolutionChainDao
import com.kanoyatech.snapdex.data.local.mappers.toPokemon
import com.kanoyatech.snapdex.domain.models.EvolutionChain
import com.kanoyatech.snapdex.domain.models.PokemonId
import com.kanoyatech.snapdex.domain.repositories.EvolutionChainRepository
import java.util.Locale

class EvolutionChainRepositoryImpl(
    private val localEvolutionChain: EvolutionChainDao
): EvolutionChainRepository {
    override suspend fun getEvolutionChainForPokemon(pokemonId: PokemonId, locale: Locale): EvolutionChain? {
        val evolutionChainWithRelations = localEvolutionChain.getFor(pokemonId)
            ?: return null

        return EvolutionChain(
            startingPokemon = evolutionChainWithRelations.startingPokemon.toPokemon(locale),
            evolutions = evolutionChainWithRelations.evolvesTo.map { evolutionChainLink ->
                evolutionChainLink.minLevel to evolutionChainLink.pokemon.toPokemon(locale)
            }.toMap()
        )
    }
}