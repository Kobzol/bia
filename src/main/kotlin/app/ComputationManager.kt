package app

import algorithm.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class ComputationManager(model: FunctionModel,
                         var iterationCount: Int)
{
    private val modelChangedStream = PublishSubject.create<FunctionModel>()
    private val computationStateStream = PublishSubject.create<Boolean>()
    private val populationStream = PublishSubject.create<Population>()

    private val subManager = SubscriptionManager()

    var generation: Population? = null
        set(value) {
            field = value
            this.populationStream.onNext(value!!)
        }

    var model: FunctionModel = model
        set(value) {
            field = value
            this.modelChangedStream.onNext(value)
            this.stopComputation()
        }

    val onModelChanged: Observable<FunctionModel> = this.modelChangedStream
    val onStateChanged: Observable<Boolean> = this.computationStateStream
    val onPopulationGenerated: Observable<Population> = this.populationStream

    fun startComputation(algorithm: Algorithm)
    {
        System.out.println("Computation started")

        val computation = this.createComputation(algorithm)
        this.computationStateStream.onNext(true)

        this.subManager += computation.populationStream
                .sample(250, TimeUnit.MILLISECONDS)
                .subscribe({ population ->
                    this.generation = population
                }, { error ->
                    System.err.println(error)
                    error.printStackTrace()
                }, {
                    this.stopComputation()
                    this.generation = computation.algorithm.population

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

    private fun createComputation(algorithm: Algorithm): Computation
    {
        return Computation(
                algorithm,
                AlgorithmRunner()
                        .iterate(algorithm, this.iterationCount)
                        .subscribeOn(Schedulers.computation())
        )
    }
}
