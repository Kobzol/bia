package algorithm.simanneal

import algorithm.*
import java.util.*

class SimulatedAnnealing(private var temperature: Float,
                         private val alpha: Float,
                         private val area: Float,
                         bounds: Array<Bounds>,
                         evaluator: FitnessEvaluator): Algorithm(bounds, evaluator)
{
    private val random = Random()
    private val generator = PopulationGenerator()
    override lateinit var population: Population

    init
    {
        val pop = this.generator.generateAreaPopulation(1000, bounds)
        this.evaluator.evaluate(pop)
        this.population = listOf(this.evaluator.findBest(pop))
    }

    override fun runIteration(): Population
    {
        val individual = this.generator.generateAreaPopulationAround(1, this.population[0], this.area)
        this.evaluator.evaluate(individual)

        if (individual[0].fitness!! > this.population[0].fitness!!)
        {
            this.population = individual
        }
        else
        {
            val nonce = this.random.nextFloat()
            val diff = population[0].fitness!! - individual[0].fitness!!
            if (nonce < Math.exp(-(diff / this.temperature).toDouble()))
            {
                this.population = individual
            }
        }

        this.temperature *= this.alpha

        return this.population
    }

    override fun isFinished(): Boolean
    {
        return this.temperature < 0.000001f
    }
}
