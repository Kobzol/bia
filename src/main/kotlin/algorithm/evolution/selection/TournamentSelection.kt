package algorithm.evolution.selection

import algorithm.FitnessEvaluator
import algorithm.Individual
import algorithm.Population
import java.util.*
import java.util.Collections.swap

class TournamentSelection(private val tournamentFraction: Float,
                          private val count: Int): Selection()
{
    private val random = Random()

    override fun select(population: ArrayList<Individual>, evaluator: FitnessEvaluator): Population
    {
        this.assignFitness(population, evaluator)

        val selected = arrayListOf(evaluator.findBest(population))
        val tournamentSize = (population.size * this.tournamentFraction).toInt()
        var size = population.size

        for (i in 0 until this.count - 1)
        {
            var best = this.random.nextInt(size)

            for (j in 0 until tournamentSize - 1)
            {
                val ind = this.random.nextInt(size)
                if (population[ind].fitness!! > population[best].fitness!!)
                {
                    best = ind
                }
            }
            selected.add(population[best])
            swap(population, best, size - 1)
            size -= 1
        }

        return selected
    }
}
