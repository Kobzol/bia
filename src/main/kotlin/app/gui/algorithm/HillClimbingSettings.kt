package app.gui.algorithm

import algorithm.Algorithm
import algorithm.FitnessEvaluator
import algorithm.hillclimbing.HillClimbing
import app.FunctionModel
import javax.swing.JComponent
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class HillClimbingSettings(name: String): AlgorithmSettings(name)
{
    private val defaultIterations = 100

    private var populationSize: JTextField? = null

    override fun createGUI(root: JComponent)
    {
        this.populationSize = JTextField(this.defaultIterations.toString())
        this.populationSize?.document?.addDocumentListener(object : DocumentListener {
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
        })

        root.add(this.populationSize)
    }

    override fun createAlgorithm(model: FunctionModel, evaluator: FitnessEvaluator): Algorithm
    {
        return HillClimbing(
                this.populationSize?.text?.toInt() ?: this.defaultIterations,
                1.0f,
                arrayOf(model.boundsX, model.boundsY),
                evaluator
        )
    }
}
