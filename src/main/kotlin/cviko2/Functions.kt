package cviko2

// CEC 2005, 2008, 2013, 2014, 2016

interface Function
{
    fun calculate(vararg args: Float): Float
}

class Rastrigin: Function
{
    override fun calculate(vararg args: Float): Float
    {
        return 10.0f * args.size +
                (0 until args.size)
                .sumByDouble { args[it] * args[it] - 10 * Math.cos(2 * Math.PI * args[it]) }.toFloat()
    }
}

class Ackley: Function
{
    override fun calculate(vararg args: Float): Float
    {
        val x = args[0]
        val y = args[1]

        val a = -20.0f * Math.exp(-0.2 * Math.sqrt(0.5 * (x * x + y * y).toDouble())).toFloat()
        val b = -Math.exp(0.5 * (Math.cos(2 * Math.PI * x) + Math.cos(2 * Math.PI * y))).toFloat()

        return a + b + Math.E.toFloat() + 20
    }
}

class Sphere: Function
{
    override fun calculate(vararg args: Float): Float
    {
        return args.sumByDouble { (it * it).toDouble() }.toFloat()
    }
}

class Rosenbrock: Function
{
    override fun calculate(vararg args: Float): Float
    {
        var value = 0.0f
        for (i in 0 until args.size - 1)
        {
            value += 100.0f * (args[i + 1] - args[i] * args[i])
            value += (args[i] - 1.0f) * (args[i] - 1.0f)
        }
        return value
    }
}

class Beale: Function
{
    override fun calculate(vararg args: Float): Float
    {
        val x = args[0]
        val y = args[1]

        val a = Math.pow(1.5 - x + x * y, 2.0)
        val b = Math.pow(2.25 - x + x * y * y, 2.0)
        val c = Math.pow(2.625 - x + x * y * y * y, 2.0)

        return (a + b + c).toFloat()
    }
}

class GoldsteinPrice: Function
{
    override fun calculate(vararg args: Float): Float
    {
        val x = args[0]
        val y = args[1]

        val a = 1 + Math.pow(x.toDouble() + y + 1, 2.0) * (19 - 14*x + 3*x*x - 14*y + 6*x*y + 3*y*y)
        val b = 30 + Math.pow(2.0 * x - 3*y, 2.0) * (18 - 32*x + 12*x*x + 48*y - 36*x*y + 27*y*y)

        return (a * b).toFloat()
    }
}

class Booth: Function
{
    override fun calculate(vararg args: Float): Float
    {
        val x = args[0]
        val y = args[1]

        return (Math.pow(x + 2.0 * y - 7, 2.0) + Math.pow(2.0 * x + y * 5, 2.0)).toFloat()
    }
}

class Matyas: Function
{
    override fun calculate(vararg args: Float): Float
    {
        val x = args[0]
        val y = args[1]

        return (0.26 * (x*x + y*y) - 0.48*x*y).toFloat()
    }
}
