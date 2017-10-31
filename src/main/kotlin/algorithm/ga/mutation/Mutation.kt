package algorithm.ga.mutation

import algorithm.Individual
import java.util.*

abstract class Mutation(private val chance: Float)
{
    protected val random = Random()

    abstract fun mutate(individual: Individual): Individual

    fun shouldMutate(): Boolean
    {
        return this.random.nextFloat() < this.chance
    }
}
