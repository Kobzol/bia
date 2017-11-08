package algorithm.soma

import algorithm.*
import java.util.*

class SOMA(override var population: Population,
           private val pathLength: Float,
           private val step: Float,
           private val prt: Float,
           bounds: Array<Bounds>, evaluator: FitnessEvaluator): Algorithm(bounds, evaluator)
{
    private val random = Random()

    init
    {
        evaluator.evaluate(population)
        this.population = this.population.sortedByDescending { it.fitness!! }
    }

    override fun runIteration(): Population
    {
        val leader = this.population[0]
        val stepCount = (this.pathLength / this.step).toInt()

        for (i in 1 until this.population.size)
        {
            val perturbation = this.generatePerturbation(this.bounds)
            var bestPosition = this.population[i].data
            var bestFitness = this.population[i].fitness!!
            var position = bestPosition

            for (s in 0 until stepCount)
            {
                position = this.move(leader, position, perturbation)
                val fitness = this.evaluator.evaluate(position)
                if (fitness > bestFitness)
                {
                    bestFitness = fitness
                    bestPosition = position
                }
            }

            this.population[i].data = bestPosition
            this.population[i].fitness = bestFitness
        }

        this.population = this.population.sortedByDescending { it.fitness!! }
        return this.population
    }

    private fun move(leader: Individual, position: FloatArray, perturbation: FloatArray): FloatArray
    {
        return position.mapIndexed { index, element ->
            element + step * (leader.data[index] - element) * perturbation[index]
        }.toFloatArray()
    }

    private fun generatePerturbation(bounds: Array<Bounds>): FloatArray
    {
        return bounds.map {
            if (this.random.nextFloat() < this.prt) 0.0f else 1.0f
        }.toFloatArray()
    }
}
