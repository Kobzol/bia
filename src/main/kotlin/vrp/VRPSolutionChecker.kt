package vrp

import kotlin.system.exitProcess

class VRPSolutionChecker
{
    companion object
    {
        @JvmStatic
        fun main(vararg args: String)
        {
            if (args.size < 3) exitProcess(1)

            val loader = VRPLoader()
            val result = loader.loadResult(args[0])
            val instance = loader.load(args[1], args[2].toInt())
            val evaluator = VRPEvaluator(instance)

            val routes = evaluator.getVehicleRoutes(result.first, 0)
            val sum = routes.map { it.pathCost }.sum()

            System.out.println("${result.first}")
            System.out.println("Sum file: ${result.second}, sum calc: $sum")
        }
    }
}
