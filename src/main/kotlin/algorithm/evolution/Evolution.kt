package algorithm.evolution

import algorithm.FitnessEvaluator
import algorithm.Individual
import algorithm.Population
import kotlin.coroutines.experimental.buildSequence

class Evolution(private val fitnessEvaluator: FitnessEvaluator,
                private val selection: Selection)
{
    fun runGeneration(population: Population): Population
    {
        val selection = this.selection.select(population, this.fitnessEvaluator)

        return population
    }
    fun iterateGenerations(population: Population, count: Int): Sequence<Population>
    {
        return buildSequence {
            var lastPopulation = population
            for (i in 0 until count)
            {
                lastPopulation = runGeneration(lastPopulation)
                yield(lastPopulation)
            }
        }
    }
}
