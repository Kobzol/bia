package app.gui

import algorithm.AlgorithmType
import app.AlgorithmComboItem
import app.ComputationManager
import app.FunctionComboItem
import app.FunctionModel
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
    private val startButton: JButton
    private val stopButton: JButton

    init
    {
        this.layout = BoxLayout(this, BoxLayout.PAGE_AXIS)

        this.functionCombobox = createCombobox(functions)
        this.functionCombobox.addActionListener {
            this.computationManager.model = this.getSelectedModel()
        }

        this.algorithmCombobox = createCombobox(algorithms)
        this.algorithmCombobox.addActionListener{
            this.computationManager.algorithmType = this.getSelectedAlgorithm()
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

        this.startButton = JButton("Start")
        this.startButton.addActionListener {
            this.computationManager.startComputation()
        }

        this.stopButton = JButton("Stop")
        this.stopButton.addActionListener {
            this.computationManager.stopComputation()
        }
        this.stopButton.isEnabled = false

        this.add(this.functionCombobox)
        this.add(this.algorithmCombobox)
        this.add(this.iterationsField)
        this.add(this.startButton)
        this.add(this.stopButton)

        computationManager.onStateChanged.subscribe { running ->
            this.startButton.isEnabled = !running
            this.stopButton.isEnabled = running
        }
    }

    private fun getSelectedModel(): FunctionModel
    {
        return (this.functionCombobox.selectedItem as FunctionComboItem).model
    }
    private fun getSelectedAlgorithm(): AlgorithmType
    {
        return (this.algorithmCombobox.selectedItem as AlgorithmComboItem).algorithmType
    }
    private fun getSelectedIterations(): Int
    {
        return this.iterationsField.text.toIntOrNull() ?: DEFAULT_ITERATIONS
    }

    companion object
    {
        const val DEFAULT_ITERATIONS = 1000
    }
}
