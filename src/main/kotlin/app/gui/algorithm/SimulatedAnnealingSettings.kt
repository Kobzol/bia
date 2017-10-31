package app.gui.algorithm

import algorithm.Algorithm
import algorithm.FitnessEvaluator
import algorithm.simanneal.SimulatedAnnealing
import app.FunctionModel
import javax.swing.JComponent

class SimulatedAnnealingSettings(name: String): AlgorithmSettings(name)
{
    private var temperature: Float = 2000.0f
    private var temperatureDecay: Float = 0.99f
    private var area: Float = 1.0f

    override fun createGUI(root: JComponent)
    {
        this.addTextbox(root, "Maximum temperature:", this.temperature, { value ->
            this.temperature = value.toFloatOrNull() ?: 2000.0f
        })
        this.addTextbox(root, "Temperature decay:", this.temperatureDecay, { value ->
            this.temperatureDecay = value.toFloatOrNull() ?: 0.990f
        })
        this.addTextbox(root, "Generation area:", this.area, { value ->
            this.area = value.toFloatOrNull() ?: 1.0f
        })
    }

    override fun createAlgorithm(model: FunctionModel, evaluator: FitnessEvaluator): Algorithm
    {
        return SimulatedAnnealing(this.temperature, this.temperatureDecay, this.area, model.bounds, evaluator)
    }
}
