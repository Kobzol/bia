package app.gui.algorithm

import algorithm.Algorithm
import algorithm.FitnessEvaluator
import algorithm.PopulationGenerator
import algorithm.soma.*
import app.FunctionModel
import javax.swing.JComponent

class SOMASettings(name: String) : AlgorithmSettings(name)
{
    private var populationSize: Int = 50
    private var pathLength: Float = 1.5f
    private var step: Float = 0.11f
    private var prt: Float = 0.2f
    private var migration: SOMAMigrationType = SOMAMigrationType.AllToOne

    override fun createGUI(root: JComponent)
    {
        this.addTextbox(root, "Population size:", this.populationSize, { value ->
            this.populationSize = value.toIntOrNull() ?: 50
        })
        this.addTextbox(root, "Path length:", this.pathLength, { value ->
            this.pathLength = value.toFloatOrNull() ?: 1.5f
        })
        this.addTextbox(root, "Step:", this.step, { value ->
            this.step = value.toFloatOrNull() ?: 0.11f
        })
        this.addTextbox(root, "PRT chance:", this.prt, { value ->
            this.prt = value.toFloatOrNull() ?: 0.2f
        })
        this.addCombobox<SOMAMigrationType>(root, "Migration:", arrayOf(
                SettingsComboItem("AllToOne", SOMAMigrationType.AllToOne),
                SettingsComboItem("AllToAll", SOMAMigrationType.AllToAll),
                SettingsComboItem("AllToAllAdaptive", SOMAMigrationType.AllToAllAdaptive),
                SettingsComboItem("AllToRandom", SOMAMigrationType.AllToRandom)
        ), { value ->
            this.migration = value
        })
    }

    override fun createAlgorithm(model: FunctionModel, evaluator: FitnessEvaluator): Algorithm
    {
        return SOMA(PopulationGenerator().generateAreaPopulation(this.populationSize, model.bounds),
                this.pathLength,
                this.step,
                this.prt,
                this.createMigration(this.migration),
                model.bounds,
                evaluator
        )
    }

    private fun createMigration(migration: SOMAMigrationType): Migration
            = when (migration)
            {
                SOMAMigrationType.AllToOne -> AllToOne()
                SOMAMigrationType.AllToAll -> AllToAll()
                SOMAMigrationType.AllToAllAdaptive -> AllToAllAdaptive()
                SOMAMigrationType.AllToRandom -> AllToRandom()
            }
}
