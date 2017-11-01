package app.gui.algorithm

import algorithm.Algorithm
import algorithm.FitnessEvaluator
import algorithm.PopulationGenerator
import algorithm.evolution.crossover.DECrossover
import algorithm.evolution.de.DifferentialEvolution
import algorithm.evolution.mutation.DERand1
import algorithm.evolution.mutation.Mutation
import app.FunctionModel
import javax.swing.JComponent

class DESettings(name: String): AlgorithmSettings(name)
{
    private var populationSize: Int = 100
    private var crossoverChance: Float = 0.85f
    private var mutationType: MutationType = MutationType.DERand1
    private var mutationChance: Float = 0.5f

    override fun createGUI(root: JComponent)
    {
        this.addTextbox(root, "Population size:", this.populationSize, { value ->
            this.populationSize = value.toIntOrNull() ?: 100
        })
        this.addTextbox(root, "Crossover chance:", this.crossoverChance, { value ->
            this.crossoverChance = value.toFloatOrNull() ?: 0.85f
        })
        this.addCombobox<MutationType>(root, "Mutation:", arrayOf(
                SettingsComboItem("DERand1", this.mutationType)
        ), { value ->
            this.mutationType = value
        })
        this.addTextbox(root, "Mutation chance:", this.mutationChance, { value ->
            this.mutationChance = value.toFloatOrNull() ?: 0.5f
        })
    }

    override fun createAlgorithm(model: FunctionModel, evaluator: FitnessEvaluator): Algorithm
    {
        return DifferentialEvolution(
                PopulationGenerator().generateAreaPopulation(this.populationSize, model.bounds),
                DECrossover(this.crossoverChance),
                this.createMutation(this.mutationType, model),
                model.bounds, evaluator
        )
    }

    private fun createMutation(type: MutationType, model: FunctionModel): Mutation
    {
        return DERand1(this.mutationChance, model.bounds)
    }
}
