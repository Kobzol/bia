package algorithm.evolution.crossover

import algorithm.Individual
import algorithm.Population
import java.util.*

class DECrossover: Crossover
{
    private val random = Random()

    override fun crossover(chance: Float, parents: Population, individual: Individual): Individual
    {
        val orig = parents[0]
        val fixedIndex = this.random.nextInt(parents[1].data.size)

        val data = parents[1].data.mapIndexed { index, _ ->
            if (index == fixedIndex || this.random.nextFloat() < chance)
            {
                parents[1].data[index]
            }
            else orig.data[index]
        }.toFloatArray()
        return individual.cloneWithData(data)
    }
}
