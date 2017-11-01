package vrp

import java.awt.Point

data class VRPInstance(val vertices: List<Point>,
                       val demands: List<Int>,
                       val start: Int,
                       val capacity: Int,
                       val minTruckCount: Int)
