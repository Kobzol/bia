package algorithm.soma

import algorithm.Individual
import algorithm.Population

interface Migration
{
    fun getTargets(individual: Individual, population: Population): Population
    fun updateIndividual(individual: Individual, fitness: Float, position: FloatArray)
}
