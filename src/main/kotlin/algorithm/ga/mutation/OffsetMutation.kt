package algorithm.ga.mutation

import algorithm.Bounds
import algorithm.Individual
import algorithm.util.clamp

class OffsetMutation(chance: Float, bounds: Array<Bounds>): Mutation(chance, bounds)
{
    override fun mutate(individual: Individual): Individual
    {
        val mutated = Individual(individual.data.clone())
        for (i in individual.data.indices)
        {
            if (this.shouldMutate())
            {
                mutated.data[i] += this.random.nextGaussian().toFloat() * 0.2f
                mutated.data[i] = clamp(mutated.data[i], this.bounds[i].min, this.bounds[i].max)
            }
        }

        return mutated
    }
}
