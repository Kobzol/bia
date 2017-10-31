package algorithm.ga.selection

import algorithm.FitnessEvaluator
import algorithm.Individual
import algorithm.Population

abstract class Selection
{
    abstract fun select(population: ArrayList<Individual>, evaluator: FitnessEvaluator): Population

    protected fun assignFitness(population: Population, evaluator: FitnessEvaluator): Float
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
}
