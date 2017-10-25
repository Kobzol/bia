package app.gui.algorithm

import algorithm.Algorithm
import algorithm.FitnessEvaluator
import algorithm.hillclimbing.HillClimbing
import app.FunctionModel
import java.awt.Component
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class HillClimbingSettings(name: String): AlgorithmSettings(name)
{
    private val defaultIterations = 100

    private var populationSize: JTextField? = null
    private lateinit var listener: DocumentListener

    override fun createGUI(root: JComponent)
    {
        this.populationSize = JTextField(this.defaultIterations.toString())
        this.populationSize?.maximumSize = Dimension(280, 30)
        this.populationSize?.alignmentX = Component.CENTER_ALIGNMENT

        this.listener = object : DocumentListener {
            override fun changedUpdate(e: DocumentEvent?)
            {
                changeStream.onNext(this@HillClimbingSettings)
            }
            override fun insertUpdate(e: DocumentEvent?)
            {
                changeStream.onNext(this@HillClimbingSettings)
            }
            override fun removeUpdate(e: DocumentEvent?)
            {
                changeStream.onNext(this@HillClimbingSettings)
            }
        }
        this.populationSize?.document?.addDocumentListener(this.listener)

        val populationLabel = JLabel("Population size:")
        populationLabel.maximumSize = Dimension(280, 30)
        populationLabel.alignmentX = Component.CENTER_ALIGNMENT

        root.add(populationLabel)
        root.add(this.populationSize)
    }
    override fun destroyGUI()
    {
        this.populationSize?.let {
            it.document?.removeDocumentListener(this.listener)
            this.populationSize = null
        }
    }

    override fun createAlgorithm(model: FunctionModel, evaluator: FitnessEvaluator): Algorithm
    {
        return HillClimbing(
                this.populationSize?.text?.toInt() ?: this.defaultIterations,
                0.5f,
                arrayOf(model.boundsX, model.boundsY),
                evaluator
        )
    }
}
