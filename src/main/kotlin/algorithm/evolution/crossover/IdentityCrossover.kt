package algorithm.evolution.crossover

import algorithm.Individual
import algorithm.Population

class IdentityCrossover(chance: Float): Crossover(chance)
{
    override fun crossover(parents: Population, individual: Individual): Individual = individual
}
