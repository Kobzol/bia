package cviko1

class App
{
    private fun <T> swap(list: ArrayList<T>, index1: Int, index2: Int)
    {
        val tmp = list[index1]
        list[index1] = list[index2]
        list[index2] = tmp
    }

    private fun <T> recursePoints(list: ArrayList<T>, index: Int)
    {
        if (index == list.size) return // generated way

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
        val start = System.nanoTime()

        val list = ArrayList<Int>()
        list += 0 until count - 1

        this.recursePoints(list, 0)

        val time = System.nanoTime() - start
        System.out.println("$count nodes took ${time / 1000000.0} milliseconds")
    }
    companion object
    {
        @JvmStatic fun main(args: Array<String>)
        {
            val app = App()

            for (i in 3..15)
            {
                //System.out.println("$i: ${app.runPoints(i)}")
                app.runPoints(i)
            }
        }
    }
}
