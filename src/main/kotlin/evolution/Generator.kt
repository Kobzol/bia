package evolution

class Generator
{
    companion object
    {
        inline fun <T> generatePopulation(size: Int, generator: () -> T): List<T>
        {
            val population = ArrayList<T>()
            for (i in 0 until size)
            {
                population.add(generator())
            }
            return population
        }
    }
}
