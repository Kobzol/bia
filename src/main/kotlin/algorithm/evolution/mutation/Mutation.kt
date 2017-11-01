package algorithm.evolution.mutation

import algorithm.Individual
import algorithm.Population

interface Mutation
{
    fun mutate(individual: Individual, population: Population): Individual
}
