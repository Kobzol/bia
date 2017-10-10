package algorithm.hillclimbing

import algorithm.*
import java.util.*

class HillClimbing(private val populationSize: Int, private val generationArea: Float,
                   bounds: Array<Bounds>, evaluator: FitnessEvaluator): Algorithm(bounds, evaluator)
{
    private var random = Random()

    override var population: Population = PopulationGenerator.generateAreaPopulation(populationSize, bounds)
        private set

    init
    {
        this.evaluator.evaluate(this.population)
    }

    override fun runIteration(): Population
    {
        val best = this.evaluator.findBest(this.population)

        val surround = this.createSurrounding(best)
        this.evaluator.evaluate(surround)

        val newBest = this.evaluator.findBest(surround)
        if (newBest > best)
        {
            this.population = surround
        }

        return this.population
    }

    private fun createSurrounding(best: Individual): Population
    {
        val area = this.generationArea / 2.0f

        return (0 until this.populationSize).map {
            val coords = FloatArray(best.data.size)
            for (c in 0 until coords.size)
            {
                val center = best.data[c]
                val point = center + (this.random.nextGaussian() * area)

                coords[c] = point.toFloat()
            }

            Individual(coords)
        }.toList()
    }
}
