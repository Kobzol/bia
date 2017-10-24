package app.gui.algorithm

import algorithm.Algorithm
import algorithm.FitnessEvaluator
import algorithm.PopulationGenerator
import algorithm.soma.SOMA
import app.FunctionModel
import javax.swing.JComponent

class SOMASettings(name: String) : AlgorithmSettings(name)
{
    override fun destroyGUI()
    {

    }

    override fun createGUI(root: JComponent)
    {

    }

    override fun createAlgorithm(model: FunctionModel, evaluator: FitnessEvaluator): Algorithm
    {
        return SOMA(PopulationGenerator().generateAreaPopulation(50, model.bounds),
                1.5f,
                0.11f,
                0.2f,
                model.bounds,
                evaluator)
    }
}
