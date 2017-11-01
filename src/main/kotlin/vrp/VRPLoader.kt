package vrp

import java.awt.Point
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

class VRPLoader
{
    private val textNumRegex = Regex("[a-zA-Z]+\\s*:\\s*(\\d+)")
    private val coordRegex = Regex("\\s*\\d+\\s*(\\d+)\\s+(\\d+)")
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
}
