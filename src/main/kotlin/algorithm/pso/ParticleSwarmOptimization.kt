package algorithm.pso

import algorithm.*
import algorithm.util.clamp
import java.util.*

class PSOIndividual(val velocity: FloatArray,
                    data: FloatArray): Individual(data)
{
    var bestPosition: FloatArray = data.clone()
}

class ParticleSwarmOptimization(size: Int, bounds: Array<Bounds>, evaluator: FitnessEvaluator)
    : Algorithm(bounds, evaluator)
{
    private val random = Random()
    private var best: PSOIndividual
    private val velocityDecay = 0.95f
    private val omegaP = 0.1f
    private val omegaG = 0.1f
    private val inertiaStart = 0.9f
    private val inertiaEnd = 0.4f
    private var inertia = inertiaStart
    private var iteration = 0

    override val population: List<PSOIndividual>

    init
    {
        this.population = PopulationGenerator().generatePopulation(size, bounds, { data ->
            PSOIndividual(bounds.map {
                (this.random.nextFloat() - 0.5f) * 2.0f * (it.max - it.min)
            }.toFloatArray(), data)
        })
        this.evaluator.evaluate(this.population)
        this.best = this.evaluator.findBest(this.population)
        this.best = PSOIndividual(this.best.velocity.clone(), this.best.data.clone())
        this.evaluator.evaluate(this.best)
    }

    override fun runIteration(): Population
    {
        for (particle in this.population)
        {
            for (i in particle.data.indices)
            {
                val rp = this.random.nextFloat()
                val rg = this.random.nextFloat()

                particle.velocity[i] =
                        this.inertia * particle.velocity[i] +
                        this.omegaP * rp * (particle.bestPosition[i] - particle.data[i]) +
                        this.omegaG * rg * (this.best.data[i] - particle.data[i])
            }
            particle.data = particle.data
                    .zip(particle.velocity, { pos, vel -> pos + vel })
                    .mapIndexed { index, value -> clamp(value, this.bounds[index].min, this.bounds[index].max) }
                    .toFloatArray()

            val fitness = this.evaluator.evaluate(particle.data)
            if (fitness > particle.fitness!!)
            {
                particle.fitness = fitness
                particle.bestPosition = particle.data.clone()
                if (fitness > this.best.fitness!!)
                {
                    this.best = PSOIndividual(particle.velocity.clone(), particle.data.clone())
                    this.best.fitness = fitness
                }
            }
        }

        this.iteration += 1

        val factor = this.iteration / 1000.0f
        this.inertia = this.inertiaStart - ((this.inertiaStart - this.inertiaEnd) * factor)

        return this.population
    }
}
