package algorithm.util

import java.util.*
import java.util.Collections.swap

fun <T> sample(list: ArrayList<T>, count: Int, random: Random): List<T>
{
    val sample = arrayListOf<T>()
    var size = list.size

    for (i in 0 until count)
    {
        val index = random.nextInt(size)
        sample.add(list[index])
        swap(list, index, size - 1)
        size -= 1
    }

    return sample
}

fun clamp(value: Float, min: Float, max: Float): Float
{
    return Math.max(min, Math.min(max, value))
}
