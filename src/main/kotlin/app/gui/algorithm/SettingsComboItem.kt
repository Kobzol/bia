package app.gui.algorithm

class SettingsComboItem<T>(private val name: String, val value: T)
{
    override fun toString() = this.name
}
