package algorithm.soma

import algorithm.Individual
import algorithm.Population

class AllToOne: Migration
{
    override fun getTargets(individual: Individual, population: Population): Population
            = listOf(population[0])

    override fun updateIndividual(individual: Individual, fitness: Float, position: FloatArray) = Unit
}
