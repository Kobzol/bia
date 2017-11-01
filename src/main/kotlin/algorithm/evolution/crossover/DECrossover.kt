package algorithm.evolution.crossover

import algorithm.Individual
import algorithm.Population

class DECrossover(chance: Float): Crossover(chance)
{
    override fun crossover(parents: Population, individual: Individual): Individual
    {
        val orig = parents[0]
        val fixedIndex = this.random.nextInt(individual.data.size)

        return Individual(individual.data.mapIndexed { index, _ ->
            if (index == fixedIndex || this.shouldCross())
            {
                individual.data[index]
            }
            else orig.data[index]
        }.toFloatArray())
    }
}
