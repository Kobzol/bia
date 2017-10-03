package algorithm

typealias Population = List<Individual>

class Individual(val data: FloatArray)
{
    var fitness: Float? = null
    fun hasFitness() = this.fitness != null

    override fun toString(): String
    {
        return "$fitness " + this.data.map { it.toString() }
    }
}
