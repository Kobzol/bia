package algorithm.evolution.de

import algorithm.*
import algorithm.evolution.crossover.DECrossover
import algorithm.util.clamp
import algorithm.util.sample
import java.util.*

class JADE(override var population: List<Individual>,
           private val c: Float,
           bounds: Array<Bounds>, evaluator: FitnessEvaluator): Algorithm(bounds, evaluator)
{
    private val random = Random()
    private val crossover = DECrossover()
    private var archive = mutableListOf<Individual>()

    private var ucr = 0.5f
    private var uf = 0.5f

    init
    {
        this.evaluator.evaluate(this.population)
        this.population = this.population.sortedDescending()
    }

    override fun runIteration(): Population
    {
        val crs = mutableListOf(0.0f)
        val fs = mutableListOf(0.0f)

        val nextPopulation = ArrayList<Individual>()

        for (i in this.population.indices)
        {
            val current = this.population[i]
            val cri = clamp(this.ucr + (this.random.nextGaussian() * 0.1f).toFloat(), 0.0f, 1.0f)
            val fi = clamp(this.uf + (this.random.nextGaussian() * 0.1f).toFloat(), 0.1f, 1.0f)
            val best = sample(this.population.take(5), 1, this.random)[0]
            val x1 = sample(this.population, 1, this.random)[0]
            val x2 = sample(this.population.plus(this.archive), 1, this.random)[0]
            val data = current.data.mapIndexed { index, value ->
                clamp((value + fi * (best.data[index] - value) + fi * (x1.data[index] - x2.data[index])),
                        this.bounds[index].min, this.bounds[index].max)
            }.toFloatArray()

            val mutated = current.cloneWithData(data)
            val crossed = this.crossover.crossover(cri, listOf(current), mutated)

            this.evaluator.evaluate(crossed)
            if (crossed > current)
            {
                nextPopulation.add(crossed)
                this.archive.add(current)
                crs.add(cri)
                fs.add(fi)
            }
            else nextPopulation.add(current)
        }

        Collections.shuffle(this.archive)
        this.archive = this.archive.subList(0, 5)

        this.ucr = (1 - this.c) * this.ucr + this.c * crs.average().toFloat()
        this.uf = (1 - this.c) * this.uf + this.c * fs.average().toFloat()

        this.population = nextPopulation.sortedDescending()
        return this.population
    }
}
