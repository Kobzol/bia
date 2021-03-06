package algorithm

import io.reactivex.Observable

class AlgorithmRunner(private val sleep: Int = 0)
{
    fun iterate(algorithm: Algorithm, count: Int): Observable<Population>
    {
        return Observable.create<Population> { observer ->
            for (i in 0 until count)
            {
                observer.onNext(algorithm.runIteration())
                if (algorithm.isFinished()) break

                if (this.sleep > 0)
                {
                    Thread.sleep(this.sleep.toLong())
                }
            }

            observer.onComplete()
        }
    }

    fun iterateSync(algorithm: Algorithm, count: Int): Population
    {
        for (i in 0 until count)
        {
            algorithm.runIteration()
        }

        return algorithm.population
    }
}
