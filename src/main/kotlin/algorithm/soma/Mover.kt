package algorithm.soma

import algorithm.Bounds
import algorithm.Individual

interface Mover
{
    fun move(leader: Individual, position: FloatArray, perturbation: FloatArray): FloatArray
    fun generatePerturbation(bounds: Array<Bounds>): FloatArray
}
