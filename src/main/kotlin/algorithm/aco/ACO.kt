package algorithm.aco

import algorithm.Algorithm
import algorithm.Individual
import algorithm.Population
import org.junit.Assert
import tsp.TSPEvaluator
import tsp.TSPInstance
import java.util.*

class Ant(var city: Int, cityCount: Int): Individual(FloatArray(cityCount))
{
    private var index = 0

    init
    {
        this.moveTo(city)
    }

    fun moveTo(city: Int)
    {
        Assert.assertEquals(this.index < this.data.size, true)

        this.city = city
        this.data[this.index] = city.toFloat()
        this.index += 1
    }
}

class ACO(val instance: TSPInstance, val antCount: Int)
    : Algorithm(arrayOf(), TSPEvaluator(instance))
{
    var best: Ant? = null
    override val population: Population
            get() = if (this.best != null) listOf(this.best!!) else listOf()

    private val random = Random()
    private val pheromones = DoubleArray(instance.vertices.size * instance.vertices.size, { 0.05 })

    private val alfa = 1.0
    private val beta = 1.0
    private val ro = 0.5
    private val q = 100.0

    override fun runIteration(): Population
    {
        val size = this.instance.vertices.size
        val ants = (0 until this.antCount).map {
            Ant(this.random.nextInt(size), size)
        }

        for (ant in ants)
        {
            val cities = (0 until size).toMutableList()
            cities.remove(ant.city)
            while (!cities.isEmpty())
            {
                var selected = 0
                if (cities.size > 1)
                {
                    val probs = this.getProbabilities(ant, cities).mapIndexed {
                        index, value -> Pair(index, value)
                    }.sortedByDescending { it.second }
                    val p = this.random.nextDouble()
                    val sum = probs.sumByDouble { it.second }

                    Assert.assertEquals(1.0, sum, 1e-3)

                    var accum = 0.0
                    for (i in 0 until cities.size)
                    {
                        accum += probs[i].second
                        if (p < accum)
                        {
                            selected = probs[i].first
                            break
                        }
                    }
                }

                ant.moveTo(cities[selected])
                cities.removeAt(selected)
            }
        }

        for (i in 0 until this.pheromones.size)
        {
            this.pheromones[i] *= this.ro
        }

        val distances = ants.map { this.evaluator.evaluate(it.data) }

        for (a in 0 until ants.size)
        {
            val ant = ants[a]
            val dist = distances[a]

            for (i in 0 until ant.data.size)
            {
                val from = ant.data[i].toInt()
                val to = ant.data[(i + 1) % ant.data.size].toInt()
                val pheromone = this.getPheromone(from, to)
                this.setPheromone(from, to, pheromone + (this.q / dist))
            }
        }

        this.evaluator.evaluate(ants)
        val bestpop = this.evaluator.findBest(ants)
        if (this.best == null || bestpop > this.best!!)
        {
            this.best = bestpop
        }

        return this.population
    }

    private fun getProbabilities(ant: Ant, cities: List<Int>): DoubleArray
    {
        fun value(city: Int) =
                Math.pow(this.getPheromone(ant.city, city), this.alfa) *
                Math.pow(1.0 / this.distance(ant.city, city), this.beta)

        val rawValues = cities.map(::value)
        val sum = rawValues.sum()

        return cities.mapIndexed { index, _ ->  rawValues[index] / sum }.toDoubleArray()
    }

    private fun getPheromone(from: Int, to: Int): Double
    {
        return this.pheromones[from * this.instance.vertices.size + to]
    }
    private fun setPheromone(from: Int, to: Int, value: Double)
    {
        this.pheromones[from * this.instance.vertices.size + to] = value
        this.pheromones[to * this.instance.vertices.size + from] = value
    }
    private fun distance(from: Int, to: Int): Double
    {
        return this.instance.vertices[from].distance(this.instance.vertices[to])
    }
}
