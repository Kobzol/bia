package cviko2

import org.sf.surfaceplot.ISurfacePlotModel

class FunctionModel(private val function: Function,
                    private val minX: Float, private val maxX: Float,
                    private val minY: Float, private val maxY: Float,
                    private val minZ: Float, private val maxZ: Float): ISurfacePlotModel
{
    override fun calculateZ(x: Float, y: Float): Float
        = this.function.calculate(x, y)

    override fun getDispDivisions(): Int = 75
    override fun getCalcDivisions(): Int = 75
    override fun getPlotMode(): Int = ISurfacePlotModel.PLOT_MODE_SPECTRUM

    override fun getXMin(): Float = this.minX
    override fun getXMax(): Float = this.maxX
    override fun getZMin(): Float = this.minZ
    override fun getZMax(): Float = this.maxZ
    override fun getYMin(): Float = this.minY
    override fun getYMax(): Float = this.maxY

    override fun getXAxisLabel(): String = "X"
    override fun getYAxisLabel(): String = "Y"
    override fun getZAxisLabel(): String = "Z"

    override fun isDisplayXY(): Boolean = true
    override fun isDisplayZ(): Boolean = true
    override fun isMesh(): Boolean = true
    override fun isScaleBox(): Boolean = false
    override fun isDisplayGrids(): Boolean = true
    override fun isBoxed(): Boolean = true
}
