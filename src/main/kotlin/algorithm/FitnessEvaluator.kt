package algorithm

import cviko2.Function

interface FitnessEvaluator
{
    fun evaluate(individual: Individual, assign: Boolean = true): Float
    fun evaluate(data: FloatArray): Float
    fun evaluate(population: Population, assign: Boolean = true)
    {
        population.parallelStream().forEach { this.evaluate(it, assign) }
    }
    fun findBest(population: Population): Individual
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

    override fun evaluate(individual: Individual, assign: Boolean): Float
    {
        val fitness = this.normalizeFitness(this.function.calculate(*individual.data))
        if (assign)
        {
            individual.fitness = fitness
        }
        return fitness

    }

    private fun normalizeFitness(fitness: Float): Float
    {
        return if (this.maximize)
        {
            fitness
        }
        else -fitness
    }
}
