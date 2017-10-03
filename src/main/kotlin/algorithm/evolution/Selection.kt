package algorithm.evolution

import algorithm.FitnessEvaluator
import algorithm.Population

fun assignFitness(population: Population, evaluator: FitnessEvaluator): Float
{
    return population
            .parallelStream()
            .map {
                if (!it.hasFitness())
                {
                    it.fitness = evaluator.evaluate(it)
                }
                it.fitness!!
            }
            .reduce(0.0f, { a, b -> a + b })
}

interface Selection
{
    fun select(population: Population, evaluator: FitnessEvaluator): Population
}
