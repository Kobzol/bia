package app

import app.gui.ControlPanel
import app.gui.MainScreen
import app.gui.createAlgorithms
import app.gui.createFunctions

class App
{
    private val computationManager: ComputationManager
    private val mainScreen: MainScreen
    private val render = false

    init
    {
        val functions = createFunctions()
        val algorithms = createAlgorithms()

        this.computationManager = ComputationManager(
                functions[0].model,
                ControlPanel.DEFAULT_ITERATIONS
        )
        this.mainScreen = MainScreen(functions, algorithms, this.computationManager, this.render)
    }

    fun show()
    {
        this.mainScreen.isVisible = true
    }

    companion object
    {
        @JvmStatic fun main(vararg args: String)
        {
            val app = App()
            app.show()
        }
    }
}
