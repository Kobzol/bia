package app.gui

import algorithm.FunctionFitness
import algorithm.blindsearch.BlindSearch
import app.gui.algorithm.*

class AlgorithmComboItem(val settings: AlgorithmSettings)
{
    override fun toString(): String = this.settings.name
}

fun createAlgorithms(): Array<AlgorithmComboItem>
{
    return arrayOf(
            AlgorithmComboItem(JADESettings("JADE")),
            AlgorithmComboItem(JDESettings("JDE")),
            AlgorithmComboItem(DESettings("DE")),
            AlgorithmComboItem(GASettings("GA")),
            AlgorithmComboItem(PSOSettings("PSO")),
            AlgorithmComboItem(SimulatedAnnealingSettings("Simulated annealing")),
            AlgorithmComboItem(SOMASettings("SOMA")),
            AlgorithmComboItem(SimpleAlgorithmSettings("Blind search", { model, _ ->
                BlindSearch(arrayOf(model.boundsX, model.boundsY), FunctionFitness(model.function))
            } )),
            AlgorithmComboItem(HillClimbingSettings("Hill climbing"))
    )
}
