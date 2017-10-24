package app.gui.chart

import algorithm.Population
import app.FunctionModel
import surfaceplot.Point3D
import surfaceplot.SurfaceCanvas

class SurfacePlotChart(model: FunctionModel): SurfaceChart
{
    override val canvas: SurfaceCanvas = SurfaceCanvas()

    init
    {
        this.updateModel(model)
    }

    override fun updateModel(model: FunctionModel, population: Population)
    {
        model.extraVertices = population.map {
            Point3D(it.data[0], it.data[1], model.function.calculate(*it.data))
        }.toTypedArray()

        this.canvas.model = model
        this.canvas.repaint()
    }
}
