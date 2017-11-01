package algorithm.evolution.crossover

import algorithm.Individual
import algorithm.Population
import java.util.*

class SinglepointCrossover(chance: Float): Crossover(chance)
{
    override fun crossover(parents: Population, individual: Individual): Individual
    {
        if (!this.shouldCross()) return individual

        val dataSize = parents[0].data.size
        val offspring = Individual(parents[0].data.clone())
        val split = this.random.nextInt(dataSize)
        for (i in 0 until split)
        {
            offspring.data[i] = parents[1].data[i]
        }

        return offspring
    }
}
