package algorithm.evolution.mutation

import algorithm.Bounds
import algorithm.Individual
import algorithm.Population
import algorithm.util.clamp
import java.util.*

class OffsetMutation(private val chance: Float,
                     private val bounds: Array<Bounds>): Mutation
{
    private val random = Random()

    override fun mutate(individual: Individual, population: Population): Individual
    {
        val mutated = Individual(individual.data.clone())
        for (i in individual.data.indices)
        {
            if (this.random.nextFloat() < this.chance)
            {
                mutated.data[i] += this.random.nextGaussian().toFloat() * 0.2f
                mutated.data[i] = clamp(mutated.data[i], this.bounds[i].min, this.bounds[i].max)
            }
        }

        return mutated
    }
}
