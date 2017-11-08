package app.gui.algorithm

import algorithm.Algorithm
import algorithm.FitnessEvaluator
import algorithm.PopulationGenerator
import algorithm.evolution.de.JADE
import app.FunctionModel
import javax.swing.JComponent

class JADESettings(name: String): AlgorithmSettings(name)
{
    private var populationSize: Int = 100
    private var c: Float = 0.1f

    override fun createGUI(root: JComponent)
    {
        this.addTextbox(root, "Population size:", this.populationSize, { value ->
            this.populationSize = value.toIntOrNull() ?: 100
        })
        this.addTextbox(root, "c:", this.c, { value ->
            this.c = value.toFloatOrNull() ?: 0.1f
        })
    }

    override fun createAlgorithm(model: FunctionModel, evaluator: FitnessEvaluator): Algorithm
    {
        return JADE(
                PopulationGenerator().generateAreaPopulation(this.populationSize, model.bounds),
                this.c,
                model.bounds, evaluator
        )
    }
}
