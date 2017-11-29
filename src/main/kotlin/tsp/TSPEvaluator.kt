package tsp

import algorithm.FitnessEvaluator

class TSPEvaluator(private val instance: TSPInstance): FitnessEvaluator
{
    override fun evaluate(data: FloatArray): Float
    {
        var length = 0.0f

        for (i in 0 until data.size)
        {
            val city = data[i].toInt()
            val nextCity = data[(i + 1) % data.size].toInt()
            length += this.instance.vertices[city].distance(this.instance.vertices[nextCity]).toFloat()
        }

        return -length
    }
}
