package app

import algorithm.AlgorithmType

class AlgorithmComboItem(val algorithmType: AlgorithmType,
                         private val name: String)
{
    override fun toString(): String = this.name
}

fun createAlgorithms(): Array<AlgorithmComboItem>
{
    return arrayOf(
            AlgorithmComboItem(AlgorithmType.BlindSearch, "Blind search")
    )
}
