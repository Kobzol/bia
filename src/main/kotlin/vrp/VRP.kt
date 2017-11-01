package vrp

import algorithm.Bounds
import algorithm.PopulationGenerator
import algorithm.ga.GeneticAlgorithm
import algorithm.ga.crossover.SinglepointCrossover
import algorithm.ga.mutation.DiscreteMutation
import algorithm.ga.selection.TournamentSelection
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
                    .map { Bounds(0.0f, instance.minTruckCount.toFloat() - 1) }
                    .toTypedArray()

            var bestRoute = listOf<VehicleRoute>()
            var bestCost = 100000.0f
            for (iter in 0 until 20)
            {
                val ga = GeneticAlgorithm(
                        generator.generateAreaPopulationDiscrete(100, bounds),
                        1,
                        TournamentSelection(0.2f, 4),
                        SinglepointCrossover(0.85f),
                        DiscreteMutation(0.05f, bounds),
                        bounds,
                        evaluator
                )

                for (i in 0 until 2500)
                {
                    ga.runIteration()
                }

                val best = ga.population.sortedDescending()[0]
                val vehicles = evaluator.getVehicleData(best.data)
                val routes = evaluator.getVehicleRoutes(vehicles)
                val sum = routes.map { it.pathCost }.sum()
                if (sum < bestCost)
                {
                    bestCost = sum
                    bestRoute = routes
                }
                System.out.println(iter)
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
