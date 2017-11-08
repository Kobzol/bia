package algorithm.evolution.crossover

import algorithm.Individual
import algorithm.Population
import java.util.*

class SinglepointCrossover: Crossover
{
    private val random = Random()

    override fun crossover(chance: Float, parents: Population, individual: Individual): Individual
    {
        if (this.random.nextFloat() >= chance) return individual

        val dataSize = parents[0].data.size
        val offspring = parents[0].cloneWithData(parents[0].data.clone())
        val split = this.random.nextInt(dataSize)
        for (i in 0 until split)
        {
            offspring.data[i] = parents[1].data[i]
        }

        return offspring
    }
}
