package vrp

import algorithm.Bounds
import algorithm.PopulationGenerator
import algorithm.evolution.crossover.SinglepointCrossover
import algorithm.evolution.ga.GeneticAlgorithm
import algorithm.evolution.mutation.SwapMutation
import algorithm.evolution.selection.TournamentSelection
import kotlin.system.exitProcess

class VRP
{
    companion object
    {
        @JvmStatic fun main(vararg args: String)
        {
            if (args.size < 2) exitProcess(1)

            val instance = VRPLoader().load(args[0], args[1].toInt())
            val evaluator = VRPEvaluator(instance)
            val generator = PopulationGenerator()
            val bounds = (0 until instance.vertices.size - 1)
                    .map { Bounds(0.0f, instance.minTruckCount.toFloat() - 0.01f) }
                    .toTypedArray()

            var bestRoute = listOf<VehicleRoute>()
            var bestCost = 100000.0f
            for (iter in 0 until 5)
            {
                val pop = generator.generateAreaPopulationDiscrete(200, bounds)
                val ga = GeneticAlgorithm(
                        pop,
                        1,
                        TournamentSelection(0.2f, 20),
                        SinglepointCrossover(),
                        0.85f,
                        SwapMutation(bounds.map { Bounds(it.min, it.max - 0.1f) }.toTypedArray()),
                        0.1f,
                        bounds,
                        evaluator
                )

                for (i in 0 until 2000)
                {
                    ga.runIteration()
                }

                val best = ga.population.sortedDescending().find {
                    val vehicles = evaluator.getVehicleData(it.data)
                    val routes = evaluator.getVehicleRoutes(vehicles)
                    evaluator.isValid(routes)
                }

                best?.let {
                    val vehicles = evaluator.getVehicleData(best.data)
                    val routes = evaluator.getVehicleRoutes(vehicles)
                    val sum = routes.map { it.pathCost }.sum()
                    if (sum < bestCost && evaluator.isValid(routes))
                    {
                        bestCost = sum
                        bestRoute = routes
                    }
                    System.out.println(iter)
                }
            }

            System.out.println("Cost: $bestCost, valid: ${evaluator.isValid(bestRoute)}")

            for (i in bestRoute.indices)
            {
                System.out.println("Route #${i + 1}: ${bestRoute[i].vertices}, " +
                        "cost: ${bestRoute[i].pathCost}, " +
                        "demand: ${bestRoute[i].demandCost}, " +
                        "demands: ${bestRoute[i].vertices.map { instance.demands[it] }}"
                )
            }
        }
    }
}
