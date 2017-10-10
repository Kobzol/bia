package app.gui.algorithm

import algorithm.Algorithm
import algorithm.FitnessEvaluator
import app.FunctionModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.swing.JComponent

abstract class AlgorithmSettings(val name: String)
{
    protected val changeStream = PublishSubject.create<AlgorithmSettings>()

    val onChange: Observable<AlgorithmSettings> = this.changeStream

    abstract fun createGUI(root: JComponent)
    abstract fun createAlgorithm(model: FunctionModel, evaluator: FitnessEvaluator): Algorithm
}
