package app.gui.algorithm

import algorithm.Algorithm
import algorithm.FitnessEvaluator
import algorithm.evolution.strategy.EvolutionStrategy
import app.FunctionModel

class EvolutionStrategySettings(name: String): AlgorithmSettings(name)
{
    override fun createAlgorithm(model: FunctionModel, evaluator: FitnessEvaluator): Algorithm
    {
        return EvolutionStrategy(100, model.bounds, evaluator)
    }
}
