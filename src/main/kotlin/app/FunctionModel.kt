package app

import algorithm.Bounds
import cviko2.Function
import surfaceplot.ISurfacePlotModel
import surfaceplot.Point3D
import java.awt.Color

class FunctionModel(val function: Function,
                    val boundsX: Bounds,
                    val boundsY: Bounds,
                    val boundsZ: Bounds,
                    private val color: Color = Color.RED): ISurfacePlotModel
{
    private var vertices: Array<Point3D> = arrayOf()

    override fun calculateZ(x: Float, y: Float): Float
        = this.function.calculate(x, y)

    override fun getExtraVertices(): Array<Point3D> = this.vertices
    fun setExtraVertices(vertices: Array<Point3D>)
    {
        this.vertices = vertices
    }
    override fun getExtraColor(): Color = this.color

    override fun getDispDivisions(): Int = 75
    override fun getCalcDivisions(): Int = 75
    override fun getPlotMode(): Int = ISurfacePlotModel.PLOT_MODE_DUALSHADE

    override fun getXMin(): Float = this.boundsX.min
    override fun getXMax(): Float = this.boundsX.max
    override fun getYMin(): Float = this.boundsY.min
    override fun getYMax(): Float = this.boundsY.max
    override fun getZMin(): Float = this.boundsZ.min
    override fun getZMax(): Float = this.boundsZ.max

    override fun getXAxisLabel(): String = "X"
    override fun getYAxisLabel(): String = "Y"
    override fun getZAxisLabel(): String = "Z"

    override fun isDisplayXY(): Boolean = true
    override fun isDisplayZ(): Boolean = true
    override fun isMesh(): Boolean = false
    override fun isScaleBox(): Boolean = false
    override fun isDisplayGrids(): Boolean = true
    override fun isBoxed(): Boolean = true
}
