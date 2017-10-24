package app.gui.chart

import algorithm.Population
import app.FunctionModel
import java.awt.Component

interface SurfaceChart
{
    val canvas: Component
    fun updateModel(model: FunctionModel, population: Population = listOf())
}
