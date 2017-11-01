package algorithm.evolution.de

import algorithm.*
import algorithm.evolution.crossover.Crossover
import algorithm.evolution.mutation.Mutation

class DifferentialEvolution(override var population: Population,
                            private val crossover: Crossover,
                            private val mutation: Mutation,
                            bounds: Array<Bounds>, evaluator: FitnessEvaluator): Algorithm(bounds, evaluator)
{
    init
    {
        this.evaluator.evaluate(this.population)
    }

    override fun runIteration(): Population
    {
        val nextPopulation = ArrayList<Individual>()
        val pop = ArrayList<Individual>(this.population)

        for (i in this.population.indices)
        {
            val mutated = this.mutation.mutate(this.population[i], pop)
            val crossed = this.crossover.crossover(listOf(this.population[i]), mutated)

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
