package app

import org.sf.surfaceplot.SurfaceCanvas
import java.awt.BorderLayout
import javax.swing.JFrame
import javax.swing.WindowConstants


// https://sourceforge.net/p/javasurfaceplot/code/HEAD/tree/trunk/src/net/sf/surfaceplot/
class App: JFrame("BIA")
{
     fun start()
    {
        val functions = createFunctions()
        val combobox = createFunctionCombobox(functions)

        val canvas = SurfaceCanvas()
        canvas.setModel(functions[0].model)

        combobox.addActionListener {
            val item = combobox.selectedItem as FunctionComboItem
            canvas.setModel(item.model)
            canvas.repaint()
        }

        this.contentPane.layout = BorderLayout()
        this.contentPane.add(combobox, BorderLayout.PAGE_START)
        this.contentPane.add(canvas, BorderLayout.CENTER)
        canvas.repaint()

        this.setSize(800, 600)

        this.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        this.isVisible = true
    }

    companion object
    {
        @JvmStatic fun main(vararg args: String)
        {
            val app = App()
            app.start()
        }
    }
}
