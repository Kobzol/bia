package algorithm.soma

import algorithm.Individual
import algorithm.Population
import org.junit.Assert
import java.util.*

class AllToRandom : Migration
{
    private val random = Random()

    override fun getTargets(individual: Individual, population: Population): Population
    {
        Assert.assertEquals(population.size > 1, true)

        while (true)
        {
            val sample = population[this.random.nextInt(population.size)]
            if (sample != individual) return listOf(sample)
        }
    }

    override fun updateIndividual(individual: Individual, fitness: Float, position: FloatArray) = Unit
}
