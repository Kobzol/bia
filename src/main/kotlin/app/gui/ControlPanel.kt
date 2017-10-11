package app.gui

import algorithm.Algorithm
import algorithm.Bounds
import algorithm.FunctionFitness
import algorithm.PopulationGenerator
import app.ComputationManager
import app.FunctionModel
import app.SubscriptionManager
import app.gui.algorithm.AlgorithmSettings
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import surfaceplot.Point3D
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class ControlPanel(functions: Array<FunctionComboItem>,
                   algorithms: Array<AlgorithmComboItem>,
                   val computationManager: ComputationManager): JPanel()
{
    private val functionCombobox: JComboBox<FunctionComboItem>
    private val algorithmCombobox: JComboBox<AlgorithmComboItem>

    private val iterationsField: JTextField
    private val generatePopulationButton: JButton
    private val algorithmContainer: JPanel
    private val startButton: JButton
    private val stopButton: JButton

    private val subscriptionManager = SubscriptionManager()

    private val populationGenerateStream = PublishSubject.create<Int>()

    val onPopulateGeneration: Observable<Int> = this.populationGenerateStream

    init
    {
        this.layout = BoxLayout(this, BoxLayout.PAGE_AXIS)

        this.functionCombobox = createCombobox(functions)
        this.functionCombobox.addActionListener {
            this.computationManager.model = this.getSelectedModel()
        }

        this.algorithmCombobox = createCombobox(algorithms)
        this.algorithmCombobox.addActionListener{
            this.computationManager.stopComputation()
            this.createAlgorithmGUI(this.getSelectedAlgorithmSettings())
        }

        this.iterationsField = JTextField(DEFAULT_ITERATIONS.toString())
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

        this.algorithmContainer = JPanel()
        this.algorithmContainer.layout = BoxLayout(this.algorithmContainer, BoxLayout.PAGE_AXIS)
        this.createAlgorithmGUI(algorithms[0].settings)

        this.generatePopulationButton = JButton("Generate population")
        this.generatePopulationButton.addActionListener {
            this.populationGenerateStream.onNext(100)
        }

        this.startButton = JButton("Start")
        this.startButton.addActionListener {
            this.computationManager.startComputation(this.createAlgorithm())
        }

        this.stopButton = JButton("Stop")
        this.stopButton.addActionListener {
            this.computationManager.stopComputation()
        }
        this.stopButton.isEnabled = false

        this.add(this.functionCombobox)
        this.add(this.algorithmCombobox)
        this.add(this.iterationsField)
        this.add(this.algorithmContainer)
        this.add(this.generatePopulationButton)
        this.add(this.startButton)
        this.add(this.stopButton)

        computationManager.onStateChanged.subscribe { running ->
            this.startButton.isEnabled = !running
            this.stopButton.isEnabled = running
        }
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

        this.algorithmContainer.removeAll()
        settings.createGUI(this.algorithmContainer)

        this.subscriptionManager += settings.onChange.subscribe {
            this.computationManager.stopComputation()
        }
        this.algorithmContainer.repaint()
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
