package app.gui.algorithm

import algorithm.Algorithm
import algorithm.FitnessEvaluator
import algorithm.ga.GeneticAlgorithm
import algorithm.ga.crossover.Crossover
import algorithm.ga.crossover.IdentityCrossover
import algorithm.ga.crossover.SinglepointCrossover
import algorithm.ga.mutation.Mutation
import algorithm.ga.mutation.OffsetMutation
import algorithm.ga.selection.Selection
import algorithm.ga.selection.TournamentSelection
import app.FunctionModel
import javax.swing.JComponent

class GASettings(name: String): AlgorithmSettings(name)
{
    private var populationSize: Int = 100
    private var elitismCount: Int = 1
    private var selection: Selection = TournamentSelection(0.1f, 20)
    private var crossover: Crossover = SinglepointCrossover(0.85f)
    private var mutation: Mutation = OffsetMutation(0.02f)

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
                SettingsComboItem("Identity", IdentityCrossover(0.85f))
        ), { value ->
            this.crossover = value
        })
        this.addCombobox<Mutation>(root, "Mutation:", arrayOf(
                SettingsComboItem("Offset", this.mutation)
        ), { value ->
            this.mutation = value
        })
    }

    override fun createAlgorithm(model: FunctionModel, evaluator: FitnessEvaluator): Algorithm
    {
        return GeneticAlgorithm(
                this.populationSize,
                this.elitismCount,
                this.selection,
                this.crossover,
                this.mutation,
                model.bounds, evaluator
        )
    }
}
