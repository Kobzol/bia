package app.gui.algorithm

import algorithm.Algorithm
import algorithm.FitnessEvaluator
import algorithm.pso.ParticleSwarmOptimization
import app.FunctionModel
import javax.swing.JComponent

class PSOSettings(name: String): AlgorithmSettings(name)
{
    override fun destroyGUI()
    {

    }

    override fun createGUI(root: JComponent)
    {

    }

    override fun createAlgorithm(model: FunctionModel, evaluator: FitnessEvaluator): Algorithm
    {
        return ParticleSwarmOptimization(100, model.bounds, evaluator)
    }
}
