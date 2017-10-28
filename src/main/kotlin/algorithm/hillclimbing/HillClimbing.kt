package algorithm.hillclimbing

import algorithm.*
import java.util.*

class HillClimbing(private val populationSize: Int, private val generationArea: Float,
                   bounds: Array<Bounds>, evaluator: FitnessEvaluator): Algorithm(bounds, evaluator)
{
    private val generator = PopulationGenerator()

    override var population: Population = this.generator.generateAreaPopulation(populationSize, bounds)
        private set

    init
    {
        this.evaluator.evaluate(this.population)
    }

    override fun runIteration(): Population
    {
        val best = this.evaluator.findBest(this.population)

        val surround = this.generator.generateAreaPopulationAround(this.populationSize, best,
                this.bounds, this.generationArea)
        this.evaluator.evaluate(surround)

        val newBest = this.evaluator.findBest(surround)
        if (newBest > best)
        {
            this.population = surround
        }

        return this.population
    }
}
