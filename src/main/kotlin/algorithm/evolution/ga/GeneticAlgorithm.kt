package algorithm.evolution.ga

import algorithm.*
import algorithm.evolution.crossover.Crossover
import algorithm.evolution.mutation.Mutation
import algorithm.evolution.selection.Selection
import algorithm.util.sample
import java.util.*

class GeneticAlgorithm(override var population: Population,
                       private val elitismCount: Int,
                       private val selection: Selection,
                       private val crossover: Crossover,
                       private val crossoverChance: Float,
                       private val mutation: Mutation,
                       private val mutationChance: Float,
                       bounds: Array<Bounds>, evaluator: FitnessEvaluator): Algorithm(bounds, evaluator)
{
    private val random = Random()

    init
    {
        this.evaluator.evaluate(this.population)
    }

    override fun runIteration(): Population
    {
        val population = ArrayList<Individual>(this.population.size)
        if (this.elitismCount > 0)
        {
            population.addAll(this.population.sortedDescending().take(this.elitismCount))
        }

        val parents = this.selection.select(this.population as ArrayList<Individual>, this.evaluator)
        for (i in this.elitismCount until this.population.size)
        {
            val selectedParents = sample(parents as ArrayList<Individual>, 2, this.random)
            val ind = this.mutation.mutate(this.mutationChance,
                    this.crossover.crossover(this.crossoverChance, selectedParents, this.population[i]),
                    this.population)
            this.evaluator.evaluate(ind)

            if (ind > this.population[i])
            {
                population.add(ind)
            }
            else population.add(this.population[i])
        }

        this.population = population
        return this.population
    }
}
