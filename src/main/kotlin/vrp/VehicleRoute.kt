package vrp

data class VehicleRoute(val pathCost: Float,
                        val demandCost: Float,
                        val vertices: List<Int>)
