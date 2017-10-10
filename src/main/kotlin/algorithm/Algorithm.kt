package algorithm

abstract class Algorithm(val bounds: Array<Bounds>,
                         val evaluator: FitnessEvaluator)
{
    abstract val population: Population
    abstract fun runIteration(): Population

    fun getBestIndividual(): Individual = this.evaluator.findBest(this.population)
}
