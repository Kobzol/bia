package algorithm.evolution.mutation

import algorithm.Bounds
import algorithm.Individual
import algorithm.Population
import algorithm.evolution.mutation.Mutation
import algorithm.util.clamp
import java.util.*

class SwapMutation(val bounds: Array<Bounds>) : Mutation
{
    private val random = Random()

    override fun mutate(chance: Float, individual: Individual, population: Population): Individual
    {
        val data = individual.data.clone()
        if (this.random.nextFloat() < chance)
        {
            val pos1 = this.random.nextInt(individual.data.size)
            val pos2 = this.random.nextInt(individual.data.size)

            val tmp = data[pos1]
            data[pos1] = data[pos2]
            data[pos2] = tmp

            return Individual(data)
        }
        /*for (i in data.indices)
        {
            if (this.random.nextFloat() < chance)
            {
                data[i] += this.random.nextGaussian().toFloat() * 3.0f
                data[i] = clamp(data[i], this.bounds[i].min, this.bounds[i].max)
            }
        }*/

        return Individual(data)
    }
}
