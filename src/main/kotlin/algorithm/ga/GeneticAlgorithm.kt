package algorithm.ga

import algorithm.*
import algorithm.ga.crossover.Crossover
import algorithm.ga.mutation.Mutation
import algorithm.ga.selection.Selection
import algorithm.util.sample
import java.util.*

class GeneticAlgorithm(populationSize: Int,
                       private val elitismCount: Int,
                       private val selection: Selection,
                       private val crossover: Crossover,
                       private val mutation: Mutation,
                       bounds: Array<Bounds>, evaluator: FitnessEvaluator): Algorithm(bounds, evaluator)
{
    private val generator = PopulationGenerator()
    private val random = Random()

    override var population: Population = this.generator.generateAreaPopulation(populationSize, this.bounds)

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
            population.add(this.mutation.mutate(this.crossover.crossover(selectedParents, this.population[i])))
        }

        this.population = population
        this.evaluator.evaluate(this.population)
        return this.population
    }
}
