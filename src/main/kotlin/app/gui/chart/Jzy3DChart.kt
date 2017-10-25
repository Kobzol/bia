package app.gui.chart

import algorithm.Bounds
import algorithm.Population
import app.FunctionModel
import org.jzy3d.chart.Chart
import org.jzy3d.chart.controllers.mouse.camera.AWTCameraMouseController
import org.jzy3d.chart.factories.SwingChartComponentFactory
import org.jzy3d.colors.Color
import org.jzy3d.colors.ColorMapper
import org.jzy3d.colors.colormaps.ColorMapGrayscale
import org.jzy3d.maths.Coord3d
import org.jzy3d.maths.Range
import org.jzy3d.plot3d.builder.Builder
import org.jzy3d.plot3d.builder.Mapper
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid
import org.jzy3d.plot3d.primitives.Scatter
import org.jzy3d.plot3d.primitives.Shape
import org.jzy3d.plot3d.rendering.canvas.Quality
import java.awt.Component

class Jzy3DChart(model: FunctionModel): SurfaceChart
{
    private var surface: Shape
    private var scatter: Scatter? = null
    private var model: FunctionModel? = null

    private val chart: Chart

    override val canvas: Component
        get() = this.chart.canvas as Component

    init
    {
        this.surface = this.createSurface(model)

        this.chart = SwingChartComponentFactory.chart(Quality.Advanced)
        this.chart.scene.graph.add(this.surface)
        val controller = AWTCameraMouseController(this.chart)
        this.canvas.addMouseListener(controller)
        this.canvas.addMouseMotionListener(controller)
        this.canvas.addMouseWheelListener(controller)
    }

    override fun updateModel(model: FunctionModel, population: Population)
    {
        if (model != this.model)
        {
            this.chart.scene.graph.remove(this.surface)
            this.surface = this.createSurface(model)
            this.chart.scene.graph.add(surface)
        }

        if (this.scatter != null)
        {
            this.chart.scene.remove(this.scatter)
        }

        if (population.isNotEmpty())
        {
            this.scatter = this.createScatter(model, population)
            this.chart.scene.add(this.scatter)
        }

        this.model = model
    }

    private fun createScatter(model: FunctionModel, population: Population): Scatter
    {
        val colors = population.map { Color(1.0f, 0.0f, 0.0f) }.toTypedArray()
        return Scatter(population.map {
            Coord3d(it.data[0], it.data[1], model.function.calculate(*it.data))
        }.toTypedArray(), colors, 3.0f)
    }

    private fun createSurface(model: FunctionModel): Shape
    {
        val mapper = object : Mapper() {
            override fun f(x: Double, y: Double): Double {
                return model.function.calculate(x.toFloat(), y.toFloat()).toDouble()
            }
        }

        val steps = 80
        val surface = Builder.buildOrthonormal(OrthonormalGrid(
                this.boundsToRange(model.boundsX), steps,
                this.boundsToRange(model.boundsY), steps), mapper)
        surface.colorMapper = ColorMapper(ColorMapGrayscale(),
                model.boundsZ.min.toDouble(),
                model.boundsZ.max.toDouble(),
                Color(1.0f, 1.0f, 1.0f, .5f))
        surface.faceDisplayed = true
        surface.wireframeDisplayed = false

        return surface
    }

    private fun boundsToRange(bounds: Bounds): Range
    {
        return Range(bounds.min, bounds.max)
    }
}
