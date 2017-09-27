package evolution

class Evolution<T : Individual>(private var population: List<T>,
                                private val fitnessEvaluator: FitnessEvaluator<T>)
{
    fun getPopulation() = this.population

    fun runGeneration(): List<T>
    {
        this.assignFitness(this.population)

        return this.population
    }
    fun runGenerations(count: Int): List<T>
    {
        for (i in 0 until count)
        {
            this.runGeneration()
        }

        return this.population
    }

    private fun assignFitness(population: List<T>)
    {
        population.parallelStream().forEach {
            if (!it.hasFitness())
            {
                it.fitness = this.fitnessEvaluator.evaluate(it)
            }
        }
    }
}
