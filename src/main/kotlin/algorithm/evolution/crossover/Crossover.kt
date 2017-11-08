package algorithm.evolution.crossover

import algorithm.Individual
import algorithm.Population
import java.util.*

interface Crossover
{
    fun crossover(chance: Float, parents: Population, individual: Individual): Individual
}
