package app.gui

import algorithm.Bounds
import app.FunctionModel
import cviko2.*

class FunctionComboItem(val model: FunctionModel,
                        private val name: String)
{
    override fun toString(): String = this.name
}

fun createFunctions(): Array<FunctionComboItem>
{
    val boundsZ = Bounds(0.0f, 40.0f)

    return arrayOf(
            FunctionComboItem(FunctionModel(Pareto(), Bounds(0f, 1f), Bounds(0f, 1f), Bounds(-0.15f, 0.2f)), "Pareto"),
            FunctionComboItem(FunctionModel(Rastrigin(), Bounds(-5.12f, 5.12f), Bounds(-5.12f, 5.12f), boundsZ), "Rastrigin"),
            FunctionComboItem(FunctionModel(Ackley(), Bounds(-5f, 5f), Bounds(-5f, 5f), boundsZ), "Ackley"),
            FunctionComboItem(FunctionModel(Sphere(), Bounds(-4.5f, 4.5f), Bounds(-4.5f, 4.5f), boundsZ), "Sphere"),
            FunctionComboItem(FunctionModel(Rosenbrock(), Bounds(-4.5f, 4.5f), Bounds(-4.5f, 4.5f), boundsZ), "Rosenbrock"),
            FunctionComboItem(FunctionModel(Beale(), Bounds(-4.5f, 4.5f), Bounds(-4.5f, 4.5f), boundsZ), "Beale"),
            FunctionComboItem(FunctionModel(GoldsteinPrice(), Bounds(-2f, 2f), Bounds(-2f, 2f), boundsZ), "GoldsteinPrice"),
            FunctionComboItem(FunctionModel(Booth(), Bounds(-10f, 10f), Bounds(-10f, 10f), boundsZ), "Booth"),
            FunctionComboItem(FunctionModel(Matyas(), Bounds(-10f, 10f), Bounds(-10f, 10f), boundsZ), "Matyas")
    )
}
