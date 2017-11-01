package app.gui

import algorithm.Population
import app.ComputationManager
import app.FunctionModel
import app.SubscriptionManager
import app.gui.chart.ChartType
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
    private var lastPopulation: Population = listOf()
    private var counter = 0

    init
    {
        this.contentPane.layout = BorderLayout()

        this.chart = Jzy3DChart(functions[0].model)

        this.contentPane.add(this.controlPanel, BorderLayout.LINE_END)
        this.contentPane.add(this.chart.canvas, BorderLayout.CENTER)

        this.setSize(800, 600)

        this.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

        this.subManager += this.controlPanel.onChartChanged.subscribe { type ->
            val model = this.controlPanel.getSelectedModel()

            this.contentPane.remove(this.chart.canvas)
            this.chart = this.createChart(model, type)
            this.contentPane.add(this.chart.canvas, BorderLayout.CENTER)

            this.redrawChart(model, this.lastPopulation)
        }
        this.subManager += computationManager.onModelChanged.subscribe { model ->
            this.redrawChart(model)
        }

        this.subManager += computationManager.onPopulationGenerated.subscribe { population ->
            counter++
            this.drawPopulation(computationManager.model, population)

            if (counter % 4 == 0)
            {
                renderComponent(this.chart.canvas, "de$counter.png")
            }
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
            this.lastPopulation = best
            this.redrawChart(model, best)
        }
    }

    private fun redrawChart(model: FunctionModel, population: Population = listOf())
    {
        this.chart.updateModel(model, population)
        this.chart.canvas.revalidate()
    }

    private fun createChart(model: FunctionModel, type: ChartType): SurfaceChart
    {
        return when (type)
        {
            ChartType.Jzy3D -> Jzy3DChart(model)
            ChartType.SurfacePlot -> SurfacePlotChart(model)
        }
    }
}
