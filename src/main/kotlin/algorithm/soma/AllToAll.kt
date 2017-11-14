package algorithm.soma

import algorithm.Individual
import algorithm.Population

open class AllToAll : Migration
{
    override fun getTargets(individual: Individual, population: Population): Population
            = population

    override fun updateIndividual(individual: Individual, fitness: Float, position: FloatArray) = Unit
}
