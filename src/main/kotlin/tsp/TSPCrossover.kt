package tsp

import algorithm.Individual
import algorithm.Population
import algorithm.evolution.crossover.Crossover
import java.util.*

class TSPCrossover: Crossover
{
    val random = Random()

    override fun crossover(chance: Float, parents: Population, individual: Individual): Individual
    {
        if (this.random.nextFloat() < chance)
        {
            val data = parents[0].data.clone()
            val bounds = this.getStartStop(data.size)

            val map = BooleanArray(data.size)
            (0 until bounds.first + 1).map { map[data[it].toInt()] = true }
            (bounds.second until data.size).map { map[data[it].toInt()] = true }

            val count = (bounds.second - bounds.first) - 1
            var added = 0
            for (i in 0 until data.size)
            {
                if (!map[parents[1].data[i].toInt()])
                {
                    map[parents[1].data[i].toInt()] = true
                    data[bounds.first + 1 + added] = parents[1].data[i]
                    added += 1
                    if (added == count) break
                }
            }

            return individual.cloneWithData(data)
        }

        return individual
    }

    private fun getStartStop(size: Int): Pair<Int, Int>
    {
        val start = this.random.nextInt(size)
        val end = (start + 1 + this.random.nextInt(size - 2)) % size

        return if (start < end) Pair(start, end)
        else Pair(end, start)
    }
}
