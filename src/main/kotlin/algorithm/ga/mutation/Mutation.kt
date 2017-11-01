package algorithm.ga.mutation

import algorithm.Bounds
import algorithm.Individual
import java.util.*

abstract class Mutation(private val chance: Float, protected val bounds: Array<Bounds>)
{
    protected val random = Random()

    abstract fun mutate(individual: Individual): Individual

    fun shouldMutate(): Boolean
    {
        return this.random.nextFloat() < this.chance
    }
}
