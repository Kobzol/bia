package app.gui

import algorithm.Population
import app.*
import surfaceplot.Point3D
import surfaceplot.SurfaceCanvas
import java.awt.BorderLayout
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

class MainScreen(functions: Array<FunctionComboItem>,
                 algorithms: Array<AlgorithmComboItem>,
                 computationManager: ComputationManager): JFrame("BIA")
{
    private val canvas: SurfaceCanvas = SurfaceCanvas()
    private val controlPanel: ControlPanel = ControlPanel(functions, algorithms, computationManager)
    private val subManager = SubscriptionManager()

    init
    {
        this.contentPane.layout = BorderLayout()

        this.contentPane.add(this.controlPanel, BorderLayout.LINE_END)
        this.contentPane.add(this.canvas, BorderLayout.CENTER)

        this.canvas.model = functions[0].model
        this.canvas.repaint()

        this.setSize(800, 600)

        this.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

        this.subManager += computationManager.onModelChanged.subscribe { model ->
            this.canvas.model = model
            this.canvas.repaint()
        }
        this.subManager += computationManager.onPopulationGenerated.subscribe { population ->
            this.drawPopulation(computationManager.model, population)
        }
        this.subManager += this.controlPanel.onPopulateGeneration.subscribe { size ->
            val model = this.controlPanel.getSelectedModel()
            this.drawPopulation(model,
                    algorithm.PopulationGenerator.generateAreaPopulationDiscrete(size,
                            arrayOf(model.boundsX, model.boundsY)))
        }
    }

    private fun drawPopulation(model: FunctionModel, population: Population)
    {
        model.extraVertices = population.map {
            Point3D(it.data[0], it.data[1], model.function.calculate(*it.data))
        }.toTypedArray()

        SwingUtilities.invokeLater {
            this.canvas.refresh()
        }
    }
}
