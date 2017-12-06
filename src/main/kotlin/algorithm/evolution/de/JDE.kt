package algorithm.evolution.de

import algorithm.*
import algorithm.evolution.crossover.Crossover
import algorithm.evolution.mutation.Mutation
import java.util.*

class JDEIndividual(data: FloatArray,
                    var crossoverRate: Float,
                    var mutationRate: Float): Individual(data)
{
    override fun cloneWithData(data: FloatArray): Individual
    {
        val ind = JDEIndividual(data, this.crossoverRate, this.mutationRate)
        ind.fitness = this.fitness
        return ind
    }
}

data class ParameterChange(val change: Boolean, val f: Float, val cr: Float)

class JDE(override var population: List<JDEIndividual>,
          private val crossover: Crossover,
          private val mutation: Mutation,
          private val tau: Float,
          bounds: Array<Bounds>, evaluator: FitnessEvaluator): Algorithm(bounds, evaluator)
{
    private val random = Random()

    init
    {
        this.evaluator.evaluate(this.population)
    }

    override fun runIteration(): Population
    {
        val nextPopulation = ArrayList<JDEIndividual>()

        for (i in this.population.indices)
        {
            val ind = this.population[i]
            val individuals = mutableListOf(ind, this.evolve(ind, this.population))
            val change = this.generateChange(ind)

            if (change.change)
            {
                val newIndividual = this.evolve(JDEIndividual(this.population[i].data, change.cr, change.f),
                        this.population)
                individuals.add(newIndividual)
            }

            nextPopulation.add(individuals.sortedDescending()[0])
        }

        this.population = nextPopulation
        return this.population
    }

    private fun generateChange(individual: JDEIndividual): ParameterChange
    {
        val cr = if (this.random.nextFloat() < this.tau) this.random.nextFloat()
                 else individual.crossoverRate
        val f =  if (this.random.nextFloat() < this.tau) 0.1f + 0.9f * this.random.nextFloat()
                 else individual.mutationRate

        return ParameterChange(f != individual.mutationRate || cr != individual.crossoverRate, f, cr)
    }

    private fun evolve(individual: JDEIndividual, population: List<JDEIndividual>): JDEIndividual
    {
        val mutated = this.mutation.mutate(individual.mutationRate, individual, population)
        val crossed = this.crossover.crossover(individual.crossoverRate, listOf(individual, mutated), individual)

        this.evaluator.evaluate(crossed)

        return crossed as JDEIndividual
    }
}
