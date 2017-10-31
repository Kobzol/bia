package app.gui.algorithm

import algorithm.Algorithm
import algorithm.FitnessEvaluator
import app.FunctionModel
import app.gui.createCombobox
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.awt.Component
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

abstract class AlgorithmSettings(val name: String)
{
    private val changeStream = PublishSubject.create<AlgorithmSettings>()!!

    val onChange: Observable<AlgorithmSettings> = this.changeStream

    open fun destroyGUI()
    {

    }
    open fun createGUI(root: JComponent)
    {

    }
    abstract fun createAlgorithm(model: FunctionModel, evaluator: FitnessEvaluator): Algorithm

    protected fun <T> addTextbox(root: JComponent, title: String, value: T, onChange: (value: String) -> Unit)
    {
        val field = JTextField(value.toString())
        field.maximumSize = Dimension(280, 30)
        field.alignmentX = Component.CENTER_ALIGNMENT

        val listener = object : DocumentListener {
            override fun changedUpdate(e: DocumentEvent?)
            {
                onChange(field.text)
                changeStream.onNext(this@AlgorithmSettings)
            }
            override fun insertUpdate(e: DocumentEvent?)
            {
                onChange(field.text)
                changeStream.onNext(this@AlgorithmSettings)
            }
            override fun removeUpdate(e: DocumentEvent?)
            {
                onChange(field.text)
                changeStream.onNext(this@AlgorithmSettings)
            }
        }
        field.document?.addDocumentListener(listener)

        val label = JLabel(title)
        label.maximumSize = Dimension(280, 30)
        label.alignmentX = Component.CENTER_ALIGNMENT

        root.add(label)
        root.add(field)
    }

    protected fun <T> addCombobox(root: JComponent, title: String, items: Array<SettingsComboItem<out T>>,
                                  onChange: (value: T) -> Unit)
    {
        val combobox = createCombobox(items)
        combobox.maximumSize = Dimension(280, 30)
        combobox.alignmentX = Component.CENTER_ALIGNMENT
        combobox.addActionListener {
            onChange((combobox.selectedItem as SettingsComboItem<T>).value)
        }

        val label = JLabel(title)
        label.maximumSize = Dimension(280, 30)
        label.alignmentX = Component.CENTER_ALIGNMENT

        root.add(label)
        root.add(combobox)
    }
}
