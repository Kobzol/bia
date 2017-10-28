package algorithm.blindsearch

import algorithm.*

class BlindSearch(bounds: Array<Bounds>,
                  evaluator: FitnessEvaluator)
    : Algorithm(bounds, evaluator)
{
    private val generator = PopulationGenerator()
    override var population: Population = arrayListOf()

    init
    {
        val population = this.generator.generateAreaPopulation(1000, bounds)
        this.assignBest(population)
    }

    private fun assignBest(population: Population)
    {
        this.evaluator.evaluate(population, true)

        val best = population.sortedDescending().take(50)
        if (this.population.isEmpty())
        {
            this.population = best
        }
        else this.population = best.zip(this.population).map { pair ->
            if (pair.first.fitness!! > pair.second.fitness!!) { pair.first } else { pair.second }
        }
    }

    override fun runIteration(): Population
    {
        val generated: Population = this.generator.generateAreaPopulation(1000, this.bounds)
        val mutPop = this.population as ArrayList<Individual>

        for (i in 0 until this.population.size)
        {
            val bestLocal = this.generator.generateAreaPopulationAround(100, this.population[i], this.bounds,
                    (this.bounds[0].max - this.bounds[0].min) / 100.0f)
            this.evaluator.evaluate(bestLocal, true)
            mutPop[i] = (bestLocal + listOf(this.population[i])).maxBy { it.fitness!! }!!
        }

        this.assignBest(generated)

        return this.population
    }
}
