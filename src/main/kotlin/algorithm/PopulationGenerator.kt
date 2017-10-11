package algorithm

import java.util.*

class PopulationGenerator
{
    companion object
    {
        inline fun <T> generatePopulation(size: Int, generator: () -> T): List<T>
        {
            val population = ArrayList<T>()
            for (i in 0 until size)
            {
                population.add(generator())
            }
            return population
        }
        fun generateAreaPopulation(size: Int, bounds: Array<Bounds>): List<Individual>
        {
            val random = Random()
            val population = ArrayList<Individual>()
            for (i in 0 until size)
            {
                population.add(Individual(bounds.map {
                    it.min + random.nextFloat() * (it.max - it.min)
                }.toFloatArray()))
            }
            return population
        }
        fun generateAreaPopulationDiscrete(size: Int, bounds: Array<Bounds>): List<Individual>
        {
            val random = Random()
            val population = ArrayList<Individual>()
            for (i in 0 until size)
            {
                population.add(Individual(bounds.map {
                    (it.min.toInt() + random.nextInt((it.max - it.min).toInt())).toFloat()
                }.toFloatArray()))
            }
            return population
        }
    }
}
