package app

import algorithm.Algorithm
import algorithm.AlgorithmType
import algorithm.AlgorithmRunner
import algorithm.FunctionFitness
import algorithm.blindsearch.BlindSearch
import algorithm.Population
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class ComputationManager(model: FunctionModel,
                         var algorithmType: AlgorithmType,
                         var iterationCount: Int)
{
    private val modelChangedStream = PublishSubject.create<FunctionModel>()
    private val computationStateStream = PublishSubject.create<Boolean>()
    private val populationStream = PublishSubject.create<Population>()

    private val subManager = SubscriptionManager()

    var model: FunctionModel = model
        set(value) {
            field = value
            this.modelChangedStream.onNext(value)
            this.stopComputation()
        }

    val onModelChanged: Observable<FunctionModel> = this.modelChangedStream
    val onStateChanged: Observable<Boolean> = this.computationStateStream
    val onPopulationGenerated: Observable<Population> = this.populationStream

    fun startComputation()
    {
        System.out.println("Computation started")

        val computation = this.createComputation()
        this.computationStateStream.onNext(true)

        this.subManager += computation.populationStream
                .sample(250, TimeUnit.MILLISECONDS)
                .subscribe({ population ->
                    this.populationStream.onNext(population)
                }, System.err::println, {
                    this.stopComputation()
                    this.populationStream.onNext(computation.algorithm.population)

                    this.printBestIndividual(computation.algorithm.population)
                })
    }

    private fun printBestIndividual(population: Population)
    {
        val sorted = population.sortedByDescending { it.fitness!! }
        System.out.println(sorted[0])
    }

    fun stopComputation()
    {
        this.subManager.unsubscribe()
        System.out.println("Computation stopped")

        this.computationStateStream.onNext(false)
    }

    private fun createComputation(): Computation
    {
        val algorithm = this.createAlgorithm()
        return Computation(
                algorithm,
                AlgorithmRunner()
                        .iterate(algorithm, this.iterationCount)
                        .subscribeOn(Schedulers.computation())
        )
    }

    private fun createAlgorithm(): Algorithm
    {
        val bounds = arrayOf(this.model.boundsX, this.model.boundsY)
        val evaluator = FunctionFitness(model.function, false)

        return when (this.algorithmType)
        {
            AlgorithmType.BlindSearch -> BlindSearch(bounds, evaluator)
        }
    }
}
