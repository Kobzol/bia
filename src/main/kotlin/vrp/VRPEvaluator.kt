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
    fun getVehicleRoutes(vehicles: List<List<Int>>): List<VehicleRoute>
    {
        return vehicles.map {
            if (it.isEmpty())
            {
                return@map VehicleRoute(0.0f, 0.0f, listOf())
            }
            var pathCost = this.instance.vertices[0].distance(this.instance.vertices[it[0] + 1])
            var demandCost = this.instance.demands[it[0] + 1]
            val vertices = arrayListOf(it[0] + 1)
            for (i in 1 until it.size)
            {
                val current = this.instance.vertices[it[i] + 1]
                val next = this.instance.vertices[if (i == it.size - 1) 0 else (it[i + 1] + 1)]
                pathCost += current.distance(next)
                demandCost += this.instance.demands[it[i] + 1]
                vertices.add(it[i] + 1)
            }
            VehicleRoute(pathCost.toFloat(), demandCost.toFloat(), vertices)
        }
    }
    fun isValid(routes: List<VehicleRoute>): Boolean
    {
        return routes.all { it.demandCost <= this.instance.capacity }
    }

    override fun evaluate(data: FloatArray): Float
    {
        var fitness = 10000.0f
        val vehicles = this.getVehicleData(data)
        val routes = this.getVehicleRoutes(vehicles)

        fitness -= routes.map { it.pathCost }.sum()
        fitness -= routes.map { it.demandCost }
                .filter { it > this.instance.capacity }
                .map { this.instance.capacity }
                .sum()
        return fitness
    }
}
