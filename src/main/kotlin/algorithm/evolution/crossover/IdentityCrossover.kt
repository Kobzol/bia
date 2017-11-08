package algorithm.evolution.crossover

import algorithm.Individual
import algorithm.Population

class IdentityCrossover: Crossover
{
    override fun crossover(chance: Float, parents: Population, individual: Individual): Individual = individual
}
