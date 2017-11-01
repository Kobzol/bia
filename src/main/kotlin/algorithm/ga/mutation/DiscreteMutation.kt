package algorithm.ga.mutation

import algorithm.Bounds
import algorithm.Individual
import algorithm.util.clamp

class DiscreteMutation(chance: Float, bounds: Array<Bounds>): Mutation(chance, bounds)
{
    override fun mutate(individual: Individual): Individual
    {
        val mutated = Individual(individual.data.clone())
        for (i in individual.data.indices)
        {
            if (this.shouldMutate())
            {
                val min = this.bounds[i].min
                val max = this.bounds[i].max
                val center = (max - min) / 2

                mutated.data[i] += Math.floor((this.random.nextGaussian() * center) + center).toFloat()
                mutated.data[i] = clamp(mutated.data[i], this.bounds[i].min, this.bounds[i].max)
            }
        }

        return mutated
    }
}
