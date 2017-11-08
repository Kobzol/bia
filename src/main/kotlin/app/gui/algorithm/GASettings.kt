package app.gui.algorithm

import algorithm.Algorithm
import algorithm.FitnessEvaluator
import algorithm.PopulationGenerator
import algorithm.evolution.ga.GeneticAlgorithm
import algorithm.evolution.crossover.Crossover
import algorithm.evolution.crossover.IdentityCrossover
import algorithm.evolution.crossover.SinglepointCrossover
import algorithm.evolution.mutation.Mutation
import algorithm.evolution.mutation.OffsetMutation
import algorithm.evolution.selection.Selection
import algorithm.evolution.selection.TournamentSelection
import app.FunctionModel
import javax.swing.JComponent

class GASettings(name: String): AlgorithmSettings(name)
{
    private var populationSize: Int = 100
    private var elitismCount: Int = 1
    private var selection: Selection = TournamentSelection(0.1f, 20)
    private var crossover: Crossover = SinglepointCrossover()
    private var crossoverChance: Float = 0.85f
    private var mutationType: MutationType = MutationType.Offset
    private var mutationChance: Float = 0.05f

    override fun createGUI(root: JComponent)
    {
        this.addTextbox(root, "Population size:", this.populationSize, { value ->
            this.populationSize = value.toIntOrNull() ?: 100
        })
        this.addTextbox(root, "Elitism count:", this.elitismCount, { value ->
            this.elitismCount = value.toIntOrNull() ?: 1
        })
        this.addCombobox<Selection>(root, "Selection:", arrayOf(
                SettingsComboItem("Tournament", this.selection)
        ), { value ->
            this.selection = value
        })
        this.addCombobox(root, "Crossover:", arrayOf(
                SettingsComboItem("Singlepoint", this.crossover),
                SettingsComboItem("Identity", IdentityCrossover())
        ), { value ->
            this.crossover = value
        })
        this.addTextbox(root, "Crossover chance:", this.crossoverChance, { value ->
            this.crossoverChance = value.toFloatOrNull() ?: 0.85f
        })
        this.addCombobox<MutationType>(root, "Mutation:", arrayOf(
                SettingsComboItem("Offset", this.mutationType)
        ), { value ->
            this.mutationType = value
        })
        this.addTextbox(root, "Mutation chance:", this.mutationChance, { value ->
            this.mutationChance = value.toFloatOrNull() ?: 0.05f
        })
    }

    override fun createAlgorithm(model: FunctionModel, evaluator: FitnessEvaluator): Algorithm
    {
        return GeneticAlgorithm(
                PopulationGenerator().generateAreaPopulation(this.populationSize, model.bounds),
                this.elitismCount,
                this.selection,
                this.crossover,
                this.crossoverChance,
                this.createMutation(this.mutationType, model),
                this.mutationChance,
                model.bounds, evaluator
        )
    }

    private fun createMutation(type: MutationType, model: FunctionModel): Mutation
    {
        return when (type)
        {
            MutationType.Offset -> OffsetMutation(model.bounds)
            else -> OffsetMutation(model.bounds)
        }
    }
}
