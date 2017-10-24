package app.gui

import algorithm.FunctionFitness
import algorithm.blindsearch.BlindSearch
import app.gui.algorithm.AlgorithmSettings
import app.gui.algorithm.HillClimbingSettings
import app.gui.algorithm.SOMASettings
import app.gui.algorithm.SimpleAlgorithmSettings

class AlgorithmComboItem(val settings: AlgorithmSettings)
{
    override fun toString(): String = this.settings.name
}

fun createAlgorithms(): Array<AlgorithmComboItem>
{
    return arrayOf(
            AlgorithmComboItem(SOMASettings("SOMA")),
            AlgorithmComboItem(SimpleAlgorithmSettings("Blind search", { model, evaluator ->
                BlindSearch(arrayOf(model.boundsX, model.boundsY), FunctionFitness(model.function))
            } )),
            AlgorithmComboItem(HillClimbingSettings("Hill climbing"))
    )
}
