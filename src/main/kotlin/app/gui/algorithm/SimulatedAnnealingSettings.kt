package app.gui.algorithm

import algorithm.Algorithm
import algorithm.FitnessEvaluator
import algorithm.simanneal.SimulatedAnnealing
import app.FunctionModel
import javax.swing.JComponent

class SimulatedAnnealingSettings(name: String): AlgorithmSettings(name)
{
    override fun destroyGUI()
    {

    }

    override fun createGUI(root: JComponent)
    {

    }

    override fun createAlgorithm(model: FunctionModel, evaluator: FitnessEvaluator): Algorithm
    {
        return SimulatedAnnealing(2000.0f, 0.99f, 1.0f, model.bounds, evaluator)
    }
}
