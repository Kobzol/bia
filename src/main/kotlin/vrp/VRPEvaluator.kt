package vrp

import algorithm.FitnessEvaluator

class VRPEvaluator(private val instance: VRPInstance): FitnessEvaluator
{
    fun getVehicleData(data: FloatArray): List<List<Int>>
    {
        val vehicles = (0 until this.instance.minTruckCount).map { ArrayList<Int>() }.toList()
        for (i in data.indices)
        {
            val vehicle = data[i].toInt()
            vehicles[vehicle].add(i)
        }
        return vehicles
    }
    fun getVehicleRoutes(vehicles: List<List<Int>>, offset: Int = 1): List<VehicleRoute>
    {
        return vehicles.map {
            if (it.isEmpty())
            {
                return@map VehicleRoute(0.0f, 0.0f, listOf())
            }
            var pathCost = this.instance.vertices[0].distance(this.instance.vertices[it[0] + offset])
            var demandCost = 0.0f
            val vertices = arrayListOf<Int>()
            for (i in 0 until it.size)
            {
                val current = this.instance.vertices[it[i] + offset]
                val next = this.instance.vertices[if (i == it.size - 1) 0 else (it[i + 1] + offset)]
                pathCost += current.distance(next)
                demandCost += this.instance.demands[it[i] + offset]
                vertices.add(it[i] + offset)
            }
            VehicleRoute(pathCost.toFloat(), demandCost, vertices)
        }
    }
    fun isValid(routes: List<VehicleRoute>): Boolean
            = routes.all { it.demandCost <= this.instance.capacity }

    override fun evaluate(data: FloatArray): Float
    {
        var fitness = 10000.0f
        val vehicles = this.getVehicleData(data)
        val routes = this.getVehicleRoutes(vehicles)

        fitness -= routes.map { it.pathCost }.sum()
        fitness -= routes.map { it.demandCost }
                .map {
                    if (it > this.instance.capacity)
                    {
                        (it - this.instance.capacity) * 10.0f
                    }
                    else (this.instance.capacity - it) * 2.0f
                }
                .sum()
        return fitness
    }
}
