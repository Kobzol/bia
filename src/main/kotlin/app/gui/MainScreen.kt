package app.gui

import algorithm.Population
import app.ComputationManager
import app.FunctionModel
import app.SubscriptionManager
import app.gui.chart.Jzy3DChart
import app.gui.chart.SurfaceChart
import app.gui.chart.SurfacePlotChart
import java.awt.BorderLayout
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

class MainScreen(functions: Array<FunctionComboItem>,
                 algorithms: Array<AlgorithmComboItem>,
                 computationManager: ComputationManager): JFrame("BIA")
{
    private var chart: SurfaceChart
    private val controlPanel: ControlPanel = ControlPanel(functions, algorithms, computationManager)
    private val subManager = SubscriptionManager()

    init
    {
        this.contentPane.layout = BorderLayout()

        this.chart = SurfacePlotChart(functions[0].model)

        this.contentPane.add(this.controlPanel, BorderLayout.LINE_END)
        this.contentPane.add(this.chart.canvas, BorderLayout.CENTER)

        this.setSize(800, 600)

        this.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

        this.subManager += computationManager.onModelChanged.subscribe { model ->
            this.redrawChart(model)
        }
        this.subManager += computationManager.onPopulationGenerated.subscribe { population ->
            this.drawPopulation(computationManager.model, population)
        }
    }

    private fun drawPopulation(model: FunctionModel, population: Population)
    {
        var best = population
        if (population[0].hasFitness())
        {
            best = population.sortedDescending()
        }
        best = best.take(100)

        SwingUtilities.invokeLater {
            this.redrawChart(model, best)
        }
    }

    private fun redrawChart(model: FunctionModel, population: Population = listOf())
    {
        this.chart.updateModel(model, population)
        this.chart.canvas.revalidate()
    }
}
