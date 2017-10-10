package algorithm.blindsearch

import algorithm.*

class BlindSearch(bounds: Array<Bounds>,
                  evaluator: FitnessEvaluator)
    : Algorithm(bounds, evaluator)
{
    override val population: Population
        get() = listOf(this.best)

    private var best: Individual

    init
    {
        val population = PopulationGenerator.generateAreaPopulation(1, bounds)
        this.evaluator.evaluate(population[0])
        this.best = population[0]
    }

    override fun runIteration(): Population
    {
        val generated: Population = PopulationGenerator.generateAreaPopulation(1, this.bounds)
        val fitness = this.evaluator.evaluate(generated[0], true)
        if (fitness > this.best.fitness!!)
        {
            this.best = generated[0]
        }

        return listOf(this.best)
    }
}
