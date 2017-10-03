package algorithm

import io.reactivex.Observable

abstract class Algorithm(val bounds: Array<Bounds>,
                         val evaluator: FitnessEvaluator)
{
    abstract val population: Population
    abstract fun runIteration(): Population

    fun iterate(count: Int): Observable<Population>
    {
        return Observable.create<Population> { observer ->
            for (i in 0 until count)
            {
                observer.onNext(this.runIteration())
            }

            observer.onComplete()
        }
    }
}
