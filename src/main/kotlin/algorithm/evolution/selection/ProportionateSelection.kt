package algorithm.evolution.selection

import algorithm.FitnessEvaluator
import algorithm.Individual
import algorithm.Population
import java.util.*

class ProportionateSelection: Selection()
{
    val random = Random()

    override fun select(population: List<Individual>, evaluator: FitnessEvaluator): Population
    {
        val fitnesses = population.map { it.fitness!! }.toList()
        val sum = fitnesses.sum()
        val normalized = fitnesses.map { it / sum }

        val pop = mutableListOf<Individual>()
        for (iter in 0 until 2)
        {
            val p = this.random.nextFloat()
            var accum = 0.0f
            for (i in 0 until normalized.size)
            {
                if (accum + normalized[i] >= p)
                {
                    pop += population[i]
                    break
                }
                accum += normalized[i]
            }
        }

        return pop
    }
}
