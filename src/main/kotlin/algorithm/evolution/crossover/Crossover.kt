package algorithm.evolution.crossover

import algorithm.Individual
import algorithm.Population
import java.util.*

abstract class Crossover(private val chance: Float)
{
    protected val random = Random()

    abstract fun crossover(parents: Population, individual: Individual): Individual

    fun shouldCross(): Boolean
    {
        return this.random.nextFloat() < this.chance
    }
}
