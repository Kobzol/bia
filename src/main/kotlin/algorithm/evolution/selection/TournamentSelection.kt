package algorithm.evolution.selection

import algorithm.FitnessEvaluator
import algorithm.Individual
import algorithm.Population
import java.util.*
import java.util.Collections.swap
import kotlin.collections.ArrayList

class TournamentSelection(private val tournamentFraction: Float,
                          private val count: Int): Selection()
{
    private val random = Random()

    override fun select(population: List<Individual>, evaluator: FitnessEvaluator): Population
    {
        val pop = ArrayList(population)
        this.assignFitness(pop, evaluator)

        val selected = arrayListOf(evaluator.findBest(pop))
        val tournamentSize = (population.size * this.tournamentFraction).toInt()
        var size = population.size

        for (i in 0 until this.count - 1)
        {
            var best = this.random.nextInt(size)

            for (j in 0 until tournamentSize - 1)
            {
                val ind = this.random.nextInt(size)
                if (pop[ind].fitness!! > pop[best].fitness!!)
                {
                    best = ind
                }
            }
            selected.add(pop[best])
            swap(pop, best, size - 1)
            size -= 1
        }

        return selected
    }
}
