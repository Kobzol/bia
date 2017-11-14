package algorithm.soma

import algorithm.Individual
import algorithm.Population

class AllToAllAdaptive : AllToAll()
{
    override fun updateIndividual(individual: Individual, fitness: Float, position: FloatArray)
    {
        if (fitness > individual.fitness!!)
        {
            individual.data = position.clone()
        }
    }
}
