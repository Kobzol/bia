package app

import app.gui.ControlPanel
import app.gui.MainScreen

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
                algorithms[0].algorithmType,
                ControlPanel.DEFAULT_ITERATIONS
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
