package algorithm

typealias Population = List<Individual>

open class Individual(var data: FloatArray): Comparable<Individual>
{
    var fitness: Float? = null
    fun hasFitness() = this.fitness != null

    override fun compareTo(other: Individual): Int
    {
        return this.fitness!!.compareTo(other.fitness!!)
    }

    override fun toString(): String
    {
        return "$fitness " + this.data.map { it.toString() }
    }
}
