package app.gui

import javax.swing.JComboBox

fun <T> createCombobox(algorithms: Array<T>): JComboBox<T>
{
    val combobox = JComboBox<T>()
    algorithms.forEach { combobox.addItem(it) }
    return combobox
}
