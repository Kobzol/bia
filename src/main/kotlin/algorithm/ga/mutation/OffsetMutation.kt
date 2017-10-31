package algorithm.ga.mutation

import algorithm.Individual

class OffsetMutation(chance: Float): Mutation(chance)
{
    override fun mutate(individual: Individual): Individual
    {
        val mutated = Individual(individual.data.clone())
        for (i in 0 until individual.data.size)
        {
            if (this.shouldMutate())
            {
                mutated.data[i] += this.random.nextGaussian().toFloat() * 0.2f
            }
        }

        return mutated
    }
}
