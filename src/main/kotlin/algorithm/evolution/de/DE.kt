package algorithm.evolution.de

import algorithm.*
import algorithm.evolution.crossover.Crossover
import algorithm.evolution.mutation.Mutation

class DE(override var population: Population,
         private val crossover: Crossover,
         private val crossoverChance: Float,
         private val mutation: Mutation,
         private val mutationChance: Float,
         bounds: Array<Bounds>, evaluator: FitnessEvaluator): Algorithm(bounds, evaluator)
{
    init
    {
        this.evaluator.evaluate(this.population)
    }

    override fun runIteration(): Population
    {
        val nextPopulation = ArrayList<Individual>()

        for (i in this.population.indices)
        {
            val mutated = this.mutation.mutate(this.mutationChance, this.population[i], this.population)
            val crossed = this.crossover.crossover(this.crossoverChance, listOf(this.population[i]), mutated)

            this.evaluator.evaluate(crossed)
            if (crossed > this.population[i])
            {
                nextPopulation.add(crossed)
            }
            else nextPopulation.add(this.population[i])
        }

        this.population = nextPopulation
        return this.population
    }
}
