package vrp

import javafx.geometry.Point2D
import tsp.TSPInstance
import java.awt.Point
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.StringReader

class VRPLoader
{
    private val textNumRegex = Regex("[a-zA-Z]+\\s*:\\s*(\\d+)")
    private val coordRegex = Regex("\\s*\\d+\\s*(\\d+)\\s+(\\d+)")
    private val coordRealRegex = Regex("\\s*\\d+\\s*(\\d+\\.\\d*)\\s+(\\d+\\.\\d*)")
    private val demandRegex = Regex("\\d+\\s+(\\d+)")

    fun load(path: String, minTruckCount: Int): VRPInstance
    {
        BufferedReader(InputStreamReader(FileInputStream(path))).use {
            for (i in 0 until 3) it.readLine()
            val dimMatch = this.textNumRegex.matchEntire(it.readLine().trim())
            val dim = dimMatch!!.groupValues[1].toInt()
            it.readLine()

            val capMatch = this.textNumRegex.matchEntire(it.readLine().trim())
            val capacity = capMatch!!.groupValues[1].toInt()
            it.readLine()

            val vertices: ArrayList<Point> = arrayListOf()
            for (i in 0 until dim)
            {
                val line = it.readLine().trim()
                val match = this.coordRegex.matchEntire(line)!!
                vertices.add(Point(
                        match.groupValues[1].toInt(),
                        match.groupValues[2].toInt())
                )
            }
            it.readLine()

            val demands: ArrayList<Int> = arrayListOf()
            for (i in 0 until dim)
            {
                val line = it.readLine().trim()
                val match = this.demandRegex.matchEntire(line)!!
                demands.add(match.groupValues[1].toInt())
            }

            it.readLine()
            return VRPInstance(vertices, demands,
                    it.readLine().trim().toInt() - 1, capacity,
                    minTruckCount)
        }
    }

    fun loadTSP(data: String): TSPInstance
    {
        BufferedReader(StringReader(data)).use {
            val vertices = mutableListOf<Point2D>()

            while (true)
            {
                val line = it.readLine()
                if (line == null || line.isEmpty()) break

                val match = this.coordRealRegex.matchEntire(line)
                match?.let {
                    vertices.add(Point2D(
                            match.groupValues[2].toDouble(),
                            match.groupValues[1].toDouble())
                    )
                }
            }

            return TSPInstance(vertices)
        }
    }

    fun loadResult(path: String): Pair<List<List<Int>>, Float>
    {
        val routes: ArrayList<List<Int>> = arrayListOf()

        BufferedReader(InputStreamReader(FileInputStream(path))).use {
            while (true)
            {
                val line = it.readLine()!!
                if (line.startsWith("Route #"))
                {
                    val data = line.substring(line.indexOf(":") + 1)
                            .split(" ")
                            .filter { it.isNotEmpty() }
                            .map { it.toInt() }
                    routes.add(data)
                }
                else
                {
                    val cost = line.substring(line.indexOf(" ") + 1).toFloat()
                    return Pair(routes, cost)
                }
            }
        }

        return Pair(routes, 0.0f)
    }
}
