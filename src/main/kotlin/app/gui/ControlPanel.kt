package app.gui

import algorithm.Algorithm
import algorithm.FunctionFitness
import algorithm.PopulationGenerator
import app.ComputationManager
import app.FunctionModel
import app.SubscriptionManager
import app.gui.algorithm.AlgorithmSettings
import app.gui.chart.ChartType
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.awt.Component
import java.awt.Dimension
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class ControlPanel(functions: Array<FunctionComboItem>,
                   algorithms: Array<AlgorithmComboItem>,
                   val computationManager: ComputationManager): JPanel()
{
    private val chartCombobox: JComboBox<ChartType> = createCombobox(arrayOf(ChartType.Jzy3D, ChartType.SurfacePlot))
    private val functionCombobox: JComboBox<FunctionComboItem> = createCombobox(functions)
    private val algorithmCombobox: JComboBox<AlgorithmComboItem> = createCombobox(algorithms)

    private val iterationsField: JTextField = JTextField(DEFAULT_ITERATIONS.toString())
    private val algorithmContainer: JPanel = JPanel()
    private val generatePopulationButton: JButton = JButton("Generate population")
    private val startButton: JButton = JButton("Start")
    private val profileButton: JButton = JButton("Profile")
    private val stopButton: JButton = JButton("Stop")

    private val subscriptionManager = SubscriptionManager()
    private val chartChangedStream = PublishSubject.create<ChartType>()

    val onChartChanged: Observable<ChartType> = this.chartChangedStream

    init
    {
        this.layout = BoxLayout(this, BoxLayout.Y_AXIS)
        this.preferredSize = Dimension(250, 0)

        this.chartCombobox.addActionListener {
            this.chartChangedStream.onNext(this.chartCombobox.selectedItem as ChartType)
        }
        this.chartCombobox.maximumSize = Dimension(this.preferredSize.width, 20)
        this.chartCombobox.alignmentX = Component.CENTER_ALIGNMENT

        this.functionCombobox.addActionListener {
            this.computationManager.model = this.getSelectedModel()
        }
        this.functionCombobox.maximumSize = Dimension(this.preferredSize.width, 20)
        this.functionCombobox.alignmentX = Component.CENTER_ALIGNMENT

        this.algorithmCombobox.addActionListener{
            this.computationManager.stopComputation()
            this.createAlgorithmGUI(this.getSelectedAlgorithmSettings())
        }
        this.algorithmCombobox.maximumSize = Dimension(this.preferredSize.width, 20)
        this.algorithmCombobox.alignmentX = Component.CENTER_ALIGNMENT

        this.iterationsField.document.addDocumentListener(object : DocumentListener {
            override fun changedUpdate(e: DocumentEvent?)
            {
                computationManager.iterationCount = getSelectedIterations()
            }
            override fun insertUpdate(e: DocumentEvent?)
            {
                computationManager.iterationCount = getSelectedIterations()
            }
            override fun removeUpdate(e: DocumentEvent?)
            {
                computationManager.iterationCount = getSelectedIterations()
            }
        })
        this.iterationsField.maximumSize = Dimension(this.preferredSize.width, 30)
        this.iterationsField.alignmentX = Component.CENTER_ALIGNMENT

        this.algorithmContainer.layout = BoxLayout(this.algorithmContainer, BoxLayout.Y_AXIS)
        this.algorithmContainer.alignmentX = Component.CENTER_ALIGNMENT
        this.createAlgorithmGUI(algorithms[0].settings)

        this.generatePopulationButton.addActionListener {
            val model = this.getSelectedModel()
            val generation = PopulationGenerator().generateAreaPopulation(1000, arrayOf(model.boundsY, model.boundsY))
            this.computationManager.generation = generation
        }
        this.generatePopulationButton.alignmentX = Component.CENTER_ALIGNMENT

        this.startButton.addActionListener {
            this.computationManager.startComputation(this.createAlgorithm())
        }
        this.startButton.alignmentX = Component.CENTER_ALIGNMENT

        this.profileButton.addActionListener {
            this.profile()
        }
        this.profileButton.alignmentX = Component.CENTER_ALIGNMENT

        this.stopButton.addActionListener {
            this.computationManager.stopComputation()
        }
        this.stopButton.alignmentX = Component.CENTER_ALIGNMENT
        this.stopButton.isEnabled = false

        this.add(this.chartCombobox)
        this.add(this.functionCombobox)
        this.add(this.algorithmCombobox)

        val labelIterations = JLabel("Iterations:")
        labelIterations.alignmentX = Component.CENTER_ALIGNMENT
        this.add(labelIterations)
        this.add(this.iterationsField)
        this.add(this.algorithmContainer)
        this.add(this.generatePopulationButton)
        this.add(this.startButton)
        this.add(this.profileButton)
        this.add(this.stopButton)

        computationManager.onStateChanged.subscribe { running ->
            this.startButton.isEnabled = !running
            this.stopButton.isEnabled = running
        }
    }

    private fun profile()
    {
        val count = 10
        var sum = 0.0
        var calcSum = 0
        var zeroSum = 0
        var firstToZeroSum = 0
        val iterations = this.getSelectedIterations()

        for (i in 0 until count)
        {
            val start = System.nanoTime()

            FunctionFitness.COUNTER = 0
            val algorithm = this.createAlgorithm()

            var added = false
            for (iter in 0 until iterations)
            {
                algorithm.runIteration()

                if (!added && ArrayList<algorithm.Individual>(algorithm.population).sortedDescending()
                        .find { it.fitness == 0.0f } != null)
                {
                    firstToZeroSum += iter
                    added = true
                }
            }

            val end = System.nanoTime()

            zeroSum += algorithm.population.sortedDescending().takeWhile { it.fitness == 0.0f }.size
            calcSum += FunctionFitness.COUNTER

            val time = (end - start) / 1000000.0
            System.out.println("Time: $time ms")
            sum += time
        }

        System.out.println("Avg time: ${sum / count}, avg calc: ${calcSum / count}, " +
                "avg zero results: ${zeroSum / count}, avg iter to zero: ${firstToZeroSum / count}")
    }

    fun getSelectedModel(): FunctionModel
    {
        return (this.functionCombobox.selectedItem as FunctionComboItem).model
    }
    fun getSelectedAlgorithmSettings(): AlgorithmSettings
    {
        return (this.algorithmCombobox.selectedItem as AlgorithmComboItem).settings
    }
    fun getSelectedIterations(): Int
    {
        return this.iterationsField.text.toIntOrNull() ?: DEFAULT_ITERATIONS
    }

    private fun createAlgorithmGUI(settings: AlgorithmSettings)
    {
        this.subscriptionManager.unsubscribe()

        settings.destroyGUI()
        this.algorithmContainer.removeAll()
        settings.createGUI(this.algorithmContainer)

        this.subscriptionManager += settings.onChange.subscribe {
            this.computationManager.stopComputation()
        }
        this.algorithmContainer.revalidate()
    }

    private fun createAlgorithm(): Algorithm
    {
        val settings = this.getSelectedAlgorithmSettings()
        val model = this.getSelectedModel()
        val evaluator = FunctionFitness(model.function)

        return settings.createAlgorithm(model, evaluator)
    }

    companion object
    {
        const val DEFAULT_ITERATIONS = 1000
    }
}
