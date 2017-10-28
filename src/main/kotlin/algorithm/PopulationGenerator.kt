package algorithm

import java.util.*

class PopulationGenerator
{
    private val random = Random()

    fun <T> generatePopulation(size: Int, bounds: Array<Bounds>, generator: (data: FloatArray) -> T): List<T>
    {
        val population = ArrayList<T>()
        for (i in 0 until size)
        {
            population.add(generator(bounds.map {
                it.min + this.random.nextFloat() * (it.max - it.min)
            }.toFloatArray()))
        }
        return population
    }
    fun generateAreaPopulation(size: Int, bounds: Array<Bounds>): Population
    {
        val population = ArrayList<Individual>()
        for (i in 0 until size)
        {
            population.add(Individual(bounds.map {
                it.min + this.random.nextFloat() * (it.max - it.min)
            }.toFloatArray()))
        }
        return population
    }
    fun generateAreaPopulationDiscrete(size: Int, bounds: Array<Bounds>): Population
    {
        val population = ArrayList<Individual>()
        for (i in 0 until size)
        {
            population.add(Individual(bounds.map {
                (it.min.toInt() + this.random.nextInt((it.max - it.min).toInt())).toFloat()
            }.toFloatArray()))
        }
        return population
    }

    fun generateAreaPopulationAround(size: Int, individual: Individual, bounds: Array<Bounds>, area: Float): Population
    {
        return (0 until size).map {
            val coords = FloatArray(individual.data.size)
            for (c in 0 until coords.size)
            {
                val center = individual.data[c]
                val point = center + (this.random.nextGaussian() * area)

                // clamp to bounds
                coords[c] = Math.max(bounds[c].min, Math.min(bounds[c].max, point.toFloat()))
            }

            Individual(coords)
        }.toList()
    }
}
