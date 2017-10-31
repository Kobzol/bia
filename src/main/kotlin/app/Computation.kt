package app

import algorithm.Algorithm
import algorithm.Population
import io.reactivex.Observable

data class Computation(val algorithm: Algorithm,
                       val populationStream: Observable<Population>)
