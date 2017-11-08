package algorithm.util

import java.util.*
import java.util.Collections.swap

fun <T> sample(list: List<T>, count: Int, random: Random): List<T>
{
    val sample = mutableListOf<T>()
    val modifiedList = ArrayList<T>(list)
    var size = modifiedList.size

    for (i in 0 until count)
    {
        val index = random.nextInt(size)
        sample.add(modifiedList[index])
        swap(modifiedList, index, size - 1)
        size -= 1
    }

    return sample
}

fun clamp(value: Float, min: Float, max: Float): Float
{
    return Math.max(min, Math.min(max, value))
}
