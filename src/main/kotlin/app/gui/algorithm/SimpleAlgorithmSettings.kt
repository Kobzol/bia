package app.gui.algorithm

import algorithm.Algorithm
import algorithm.FitnessEvaluator
import app.FunctionModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.swing.JComponent

class SimpleAlgorithmSettings(name: String,
                              private val algorithmCreation:
                              (model: FunctionModel, evaluator: FitnessEvaluator) -> Algorithm)
    : AlgorithmSettings(name)
{
    override fun createGUI(root: JComponent)
    {

    }

    override fun createAlgorithm(model: FunctionModel, evaluator: FitnessEvaluator): Algorithm
    {
        return this.algorithmCreation(model, evaluator)
    }
}
