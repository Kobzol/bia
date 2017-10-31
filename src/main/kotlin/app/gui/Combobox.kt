package app.gui

import javax.swing.JComboBox

fun <T> createCombobox(items: Array<T>): JComboBox<T>
{
    val combobox = JComboBox<T>()
    items.forEach { combobox.addItem(it) }
    return combobox
}
