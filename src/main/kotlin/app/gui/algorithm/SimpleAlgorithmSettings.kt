package app.gui.algorithm

import algorithm.Algorithm
import algorithm.FitnessEvaluator
import app.FunctionModel

class SimpleAlgorithmSettings(name: String,
                              private val algorithmCreation:
                              (model: FunctionModel, evaluator: FitnessEvaluator) -> Algorithm)
    : AlgorithmSettings(name)
{
    override fun createAlgorithm(model: FunctionModel, evaluator: FitnessEvaluator): Algorithm
    {
        return this.algorithmCreation(model, evaluator)
    }
}
