package cviko1

class App
{
    private var counter: Long = 0L

    private fun factorial(n: Long): Long
    {
        if (n == 0L) return 1L

        var fact = 1L
        for (i in 2..n)
        {
            fact *= i
        }
        return fact
    }

    private fun <T> swap(list: ArrayList<T>, index1: Int, index2: Int)
    {
        val tmp = list[index1]
        list[index1] = list[index2]
        list[index2] = tmp
    }

    private fun <T> recursePoints(list: ArrayList<T>, index: Int)
    {
        if (index == list.size)
        {
            this.counter += 1
            return
        } // generated way

        this.recursePoints(list, index + 1)

        for (i in index + 1 until list.size)
        {
            this.swap(list, i, index)
            this.recursePoints(list, index + 1)
            this.swap(list, i, index)
        }
    }

    fun runPoints(count: Int)
    {
        val list = ArrayList<Int>()
        list += 0 until count

        this.counter = 0

        val start = System.nanoTime()
        this.recursePoints(list, 0)
        val time = System.nanoTime() - start

        val fact = factorial(count.toLong())
        if (this.counter != fact)
        {
            throw Exception("Wrong count, expected $fact, got ${this.counter}")
        }

        System.out.println("$count nodes took ${time / 1000000.0} milliseconds")
    }
    companion object
    {
        @JvmStatic fun main(args: Array<String>)
        {
            val app = App()
            for (i in 3..15)
            {
                app.runPoints(i - 1)
            }
        }
    }
}
