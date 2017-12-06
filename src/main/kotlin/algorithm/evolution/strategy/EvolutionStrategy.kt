package algorithm.evolution.strategy

import algorithm.*
import algorithm.util.clamp
import java.util.*
import kotlin.collections.ArrayList

class EvolutionStrategy(populationSize: Int,
                        bounds: Array<Bounds>,
                        evaluator: FitnessEvaluator) : Algorithm(bounds, evaluator)
{
    override var population = PopulationGenerator().generateAreaPopulation(populationSize, bounds)

    private val random = Random()
    private var sigma = 1.0
    private val cd = 0.817

    init
    {
        this.evaluator.evaluate(this.population)
    }

    override fun runIteration(): Population
    {
        val population = ArrayList(this.population.map { it.cloneWithData(it.data.clone()) })

        for (ind in population)
        {
            for (i in ind.data.indices)
            {
                ind.data[i] += (this.random.nextGaussian() * this.sigma).toFloat()
                ind.data[i] = clamp(ind.data[i], this.bounds[i].min, this.bounds[i].max)
            }
        }

        this.evaluator.evaluate(population)

        var mutations = 0
        for (i in population.indices)
        {
            if (this.population[i] > population[i])
            {
                population[i] = this.population[i]
            }
            else mutations += 1
        }

        val ratio = mutations / population.size.toFloat()
        if (ratio < 1.0 / 5.0)
        {
            this.sigma *= this.cd
        }
        else if (ratio > 1.0 / 5.0)
        {
            this.sigma /= this.cd
        }

        this.population = population
        return this.population
    }
}
