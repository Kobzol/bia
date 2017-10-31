package app.gui.algorithm

import algorithm.Algorithm
import algorithm.FitnessEvaluator
import algorithm.pso.ParticleSwarmOptimization
import app.FunctionModel
import javax.swing.JComponent

class PSOSettings(name: String): AlgorithmSettings(name)
{
    private var populationSize: Int = 100

    override fun createGUI(root: JComponent)
    {
        this.addTextbox(root, "Population size:", this.populationSize, { value ->
            this.populationSize = value.toIntOrNull() ?: 100
        })
    }

    override fun createAlgorithm(model: FunctionModel, evaluator: FitnessEvaluator): Algorithm
    {
        return ParticleSwarmOptimization(this.populationSize, model.bounds, evaluator)
    }
}
