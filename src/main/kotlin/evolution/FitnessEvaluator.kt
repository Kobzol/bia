package evolution

import cviko2.Function

interface FitnessEvaluator<T : Individual>
{
    fun evaluate(individual: T): Float
}

class FunctionFitness(private val function: Function,
                      private val maximize: Boolean = false): FitnessEvaluator<AreaIndividual>
{
    override fun evaluate(individual: AreaIndividual): Float
    {
        val fitness = this.function.calculate(*individual.data)
        return if (this.maximize)
        {
            fitness
        }
        else -fitness
    }
}
