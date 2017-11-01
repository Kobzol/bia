package app

import app.gui.ControlPanel
import app.gui.MainScreen
import app.gui.createAlgorithms
import app.gui.createFunctions

class App
{
    private val computationManager: ComputationManager
    private val mainScreen: MainScreen

    init
    {
        val functions = createFunctions()
        val algorithms = createAlgorithms()

        this.computationManager = ComputationManager(
                functions[0].model,
                ControlPanel.DEFAULT_ITERATIONS,
                false
        )
        this.mainScreen = MainScreen(functions, algorithms, this.computationManager)
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
