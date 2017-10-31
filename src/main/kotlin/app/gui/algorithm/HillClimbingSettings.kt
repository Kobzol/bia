package app.gui.algorithm

import algorithm.Algorithm
import algorithm.FitnessEvaluator
import algorithm.hillclimbing.HillClimbing
import app.FunctionModel
import javax.swing.JComponent

class HillClimbingSettings(name: String): AlgorithmSettings(name)
{
    private var populationSize = 100
    private var generationArea = 0.5f

    override fun createGUI(root: JComponent)
    {
        this.addTextbox(root, "Population size:", this.populationSize, { value ->
            this.populationSize = value.toIntOrNull() ?: 100
        })
        this.addTextbox(root, "Generation area:", this.generationArea, { value ->
            this.generationArea = value.toFloatOrNull() ?: 0.5f
        })
    }

    override fun createAlgorithm(model: FunctionModel, evaluator: FitnessEvaluator): Algorithm
    {
        return HillClimbing(this.populationSize, this.generationArea, arrayOf(model.boundsX, model.boundsY), evaluator)
    }
}
