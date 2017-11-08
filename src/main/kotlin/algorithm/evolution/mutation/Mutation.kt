package algorithm.evolution.mutation

import algorithm.Individual
import algorithm.Population

interface Mutation
{
    fun mutate(chance: Float, individual: Individual, population: Population): Individual
}
