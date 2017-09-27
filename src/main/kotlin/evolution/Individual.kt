package evolution

open class Individual
{
    var fitness: Float? = null

    fun hasFitness() = this.fitness != null
}

class AreaIndividual(val data: FloatArray): Individual()
{

}
