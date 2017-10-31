package algorithm.ga.crossover

import algorithm.Individual
import algorithm.Population
import java.util.*

abstract class Crossover(private val chance: Float)
{
    private val random = Random()

    abstract fun crossover(parents: Population, individual: Individual): Individual

    fun shouldCross(): Boolean
    {
        return this.random.nextFloat() < this.chance
    }
}
