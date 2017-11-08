package app.gui.algorithm

import algorithm.Algorithm
import algorithm.FitnessEvaluator
import algorithm.PopulationGenerator
import algorithm.evolution.crossover.DECrossover
import algorithm.evolution.de.JDE
import algorithm.evolution.de.JDEIndividual
import algorithm.evolution.mutation.DERand1
import algorithm.evolution.mutation.Mutation
import app.FunctionModel
import java.util.*
import javax.swing.JComponent

class JDESettings(name: String): AlgorithmSettings(name)
{
    private val random = Random()

    private var populationSize: Int = 100
    private var mutationType: MutationType = MutationType.DERand1
    private var tau: Float = 0.1f

    override fun createGUI(root: JComponent)
    {
        this.addTextbox(root, "Population size:", this.populationSize, { value ->
            this.populationSize = value.toIntOrNull() ?: 100
        })
        this.addTextbox(root, "Tau:", this.tau, { value ->
            this.tau = value.toFloatOrNull() ?: 0.1f
        })
        this.addCombobox<MutationType>(root, "Mutation:", arrayOf(
                SettingsComboItem("DERand1", this.mutationType)
        ), { value ->
            this.mutationType = value
        })
    }

    override fun createAlgorithm(model: FunctionModel, evaluator: FitnessEvaluator): Algorithm
    {
        return JDE(
                PopulationGenerator().generatePopulation(this.populationSize, model.bounds, { data ->
                    JDEIndividual(data, this.random.nextFloat(), 0.1f + (0.9f * this.random.nextFloat()))
                }),
                DECrossover(),
                this.createMutation(this.mutationType, model),
                this.tau,
                model.bounds, evaluator
        )
    }

    private fun createMutation(type: MutationType, model: FunctionModel): Mutation
    {
        return DERand1(model.bounds)
    }
}
