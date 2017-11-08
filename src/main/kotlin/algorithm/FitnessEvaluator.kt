package algorithm

import cviko2.Function

interface FitnessEvaluator
{
    fun evaluate(individual: Individual, assign: Boolean = true): Float
    {
        val fitness = this.evaluate(individual.data)
        if (assign)
        {
            individual.fitness = fitness
        }
        return fitness
    }
    fun evaluate(data: FloatArray): Float
    fun evaluate(population: Population, assign: Boolean = true)
    {
        population.parallelStream().forEach { this.evaluate(it, assign) }
    }
    fun <T : Individual> findBest(population: List<T>): T
    {
        return population.maxBy { it.fitness!! }!!
    }
}

class FunctionFitness(private val function: Function,
                      private val maximize: Boolean = false): FitnessEvaluator
{
    override fun evaluate(data: FloatArray): Float
    {
        return this.normalizeFitness(this.function.calculate(*data))
    }

    private fun normalizeFitness(fitness: Float): Float
    {
        return if (this.maximize)
        {
            fitness
        }
        else -fitness
    }

    companion object {
        var COUNTER = 0
    }
}
