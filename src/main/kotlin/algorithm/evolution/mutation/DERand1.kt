package algorithm.evolution.mutation

import algorithm.Bounds
import algorithm.Individual
import algorithm.Population
import algorithm.util.clamp
import algorithm.util.sample
import java.util.*

class DERand1(private val factor: Float,
              private val bounds: Array<Bounds>) : Mutation
{
    private val random = Random()

    override fun mutate(individual: Individual, population: Population): Individual
    {
        val random = sample(population as ArrayList<Individual>, 3, this.random)
        val data = random[0].data.clone()
        return Individual(data.mapIndexed { index, value ->
            clamp(value + this.factor * (random[1].data[index] - random[2].data[index]),
                    this.bounds[index].min, this.bounds[index].max)
        }.toFloatArray())
    }
}
