package app

import cviko2.*
import javax.swing.JComboBox

class FunctionComboItem(val model: FunctionModel,
                        val name: String)
{
    override fun toString(): String
    {
        return this.name
    }
}

fun createFunctions(): Array<FunctionComboItem>
{
    val minZ = 0.0f
    val maxZ = 75.0f

    return arrayOf(
            FunctionComboItem(FunctionModel(Rastrigin(), -5.12f, 5.12f, -5.12f, 5.12f, minZ, maxZ), "Rastrigin"),
            FunctionComboItem(FunctionModel(Ackley(), -5f, 5f, -5f, 5f, minZ, maxZ), "Ackley"),
            FunctionComboItem(FunctionModel(Sphere(), -4.5f, 4.5f, -4.5f, 4.5f, minZ, maxZ), "Sphere"),
            FunctionComboItem(FunctionModel(Rosenbrock(), -4.5f, 4.5f, -4.5f, 4.5f, minZ, maxZ), "Rosenbrock"),
            FunctionComboItem(FunctionModel(Beale(), -4.5f, 4.5f, -4.5f, 4.5f, minZ, maxZ), "Beale"),
            FunctionComboItem(FunctionModel(GoldsteinPrice(), -2f, 2f, -2f, 2f, minZ, maxZ), "GoldsteinPrice"),
            FunctionComboItem(FunctionModel(Booth(), -10f, 10f, -10f, 10f, minZ, maxZ), "Booth"),
            FunctionComboItem(FunctionModel(Matyas(), -10f, 10f, -10f, 10f, minZ, maxZ), "Matyas")
    )
}

fun createFunctionCombobox(functions: Array<FunctionComboItem>): JComboBox<FunctionComboItem>
{
    val combobox = JComboBox<FunctionComboItem>()
    functions.forEach { combobox.addItem(it) }

    return combobox
}
