package tsp

import algorithm.Algorithm
import algorithm.AlgorithmRunner
import algorithm.Individual
import algorithm.Population
import algorithm.aco.ACO
import algorithm.evolution.mutation.SwapMutation
import algorithm.evolution.selection.TournamentSelection
import algorithm.evolution.ga.GeneticAlgorithm
import app.SubscriptionManager
import app.gui.profile
import io.reactivex.schedulers.Schedulers
import javafx.application.Application
import javafx.application.Platform
import javafx.geometry.Point2D
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import vrp.VRPLoader
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors


class TSPWindow: Application()
{
    private lateinit var canvas: Canvas
    private lateinit var evaluator: TSPEvaluator
    private lateinit var instance: TSPInstance
    private lateinit var algorithm: Algorithm
    private val algorithmRunner = AlgorithmRunner(5)
    private val subManager = SubscriptionManager()

    companion object
    {
        @JvmStatic fun main(vararg args: String)
        {
            launch(TSPWindow::class.java, *args)
        }
    }

    override fun start(primaryStage: Stage)
    {
        this.instance = this.generateInstance()//this.loadInstance(this.parameters.raw.toTypedArray())
        this.evaluator = TSPEvaluator(this.instance)

        primaryStage.title = "TSP"
        val root = VBox()
        this.canvas = Canvas(600.0, 600.0)

        val calcButton = Button("Calculate")
        calcButton.setOnAction {
            this.calculate()
        }
        root.children.add(calcButton)
        root.children.add(this.canvas)

        /*val pop = this.calculateSync()
        this.drawTsp(this.instance, pop.sortedDescending()[0])*/

        profile(::createACO, 1000, 871.117f)
        profile(::createGA, 1000, 871.117f)

        primaryStage.scene = Scene(root)
        primaryStage.show()
    }

    private fun createACO(): Algorithm
    {
        this.algorithm = ACO(this.instance, this.instance.vertices.size)
        return this.algorithm
    }
    private fun createGA(): Algorithm
    {
        val pop = this.generatePopulation(this.instance, 50)
        this.algorithm = GeneticAlgorithm(pop, 0,
                TournamentSelection(0.2f, 10),
                TSPCrossover(), 0.85f,
                SwapMutation(arrayOf()), 0.05f,
                arrayOf(), this.evaluator
        )
        return this.algorithm
    }

    private fun calculateSync(): Population
    {
        var best = Float.MAX_VALUE
        var bestPop = listOf<Individual>()
        for (i in 0 until 100)
        {
            this.createACO()
            val pop = this.algorithmRunner.iterateSync(this.algorithm, 1000)
            val fitness = -this.evaluator.findBest(pop).fitness!!
            if (fitness < best)
            {
                best = fitness
                bestPop = pop
            }
            System.out.println("$i: $best")
        }

        return bestPop
    }

    private fun calculate()
    {
        this.createACO()

        this.subManager.unsubscribe()
        this.subManager += this.algorithmRunner
                .iterate(this.algorithm, 1000)
                .subscribeOn(Schedulers.computation())
                .sample(50, TimeUnit.MILLISECONDS)
                .subscribe({ pop ->
                    Platform.runLater {
                        this.drawTsp(this.instance, this.evaluator.findBest(pop))
                    }
                }, { err ->
                    err.printStackTrace()
                }, {
                    val best = this.evaluator.findBest(this.algorithm.population)
                    System.out.println("Finished, best distance: ${-best.fitness!!}")

                    Platform.runLater {
                        this.drawTsp(this.instance, best)
                    }
                })
    }

    private fun drawTsp(instance: TSPInstance, individual: Individual? = null)
    {
        val gc = this.canvas.graphicsContext2D
        gc.fill = Color.RED
        gc.stroke = Color.BLUE
        gc.lineWidth = 1.0

        gc.clearRect(0.0, 0.0, this.canvas.width, this.canvas.height)

        var maxX = instance.vertices[0].x
        var maxY = instance.vertices[0].y
        var minX = instance.vertices[0].x
        var minY = instance.vertices[0].y
        for (i in 1 until instance.vertices.size)
        {
            maxX = Math.max(maxX, instance.vertices[i].x)
            maxY = Math.max(maxY, instance.vertices[i].y)
            minX = Math.min(minX, instance.vertices[i].x)
            minY = Math.min(minY, instance.vertices[i].y)
        }

        maxX += 10
        maxY += 10
        minX -= 10
        minY -= 10

        fun tx(loc: Double): Double
        {
            return ((loc - minX) / (maxX - minX)) * this.canvas.width
        }
        fun ty(loc: Double): Double
        {
            return this.canvas.height - (((loc - minY) / (maxY - minY)) * this.canvas.height)
        }

        for (i in instance.vertices.indices)
        {
            val vert = instance.vertices[i]
            gc.fillOval(tx(vert.x), ty(vert.y), 5.0, 5.0)
            gc.fillText(i.toString(), tx(vert.x), ty(vert.y) + 15.0)
        }

        individual?.let {
            for (i in individual.data.indices)
            {
                val city = instance.vertices[individual.data[i].toInt()]
                val nextCity = instance.vertices[individual.data[(i + 1) % individual.data.size].toInt()]

                gc.strokeLine(tx(city.x), ty(city.y), tx(nextCity.x), ty(nextCity.y))
            }

            gc.fillText("Dist: ${-individual.fitness!!}", 10.0, 20.0)
        }
    }

    private fun generatePopulation(instance: TSPInstance, size: Int): Population
    {
        return (0 until size).map {
            val cities = (0 until instance.vertices.size).toMutableList()
            cities.shuffle()
            Individual(cities.map { it.toFloat() }.toFloatArray())
        }.toList()
    }

    private fun loadInstance(args: Array<out String>): TSPInstance
    {
        val url = args[0]
        var data = ""

        if (url.startsWith("http"))
        {
            val conn = URL(url).openConnection()

            BufferedReader(InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)).use { reader ->
                data = reader.lines().collect(Collectors.joining("\n"))
            }
        }
        else data = Files.readAllBytes(Paths.get(url)).toString(StandardCharsets.UTF_8)

        return VRPLoader().loadTSP(data)
    }

    private fun generateInstance(): TSPInstance
    {
        val points = mutableListOf<Point2D>()
        /*val random = Random()

        for (i in 0 until 10)
        {
            points.add(Point2D(
                    (random.nextDouble() * this.canvas.width),
                    (random.nextDouble() * this.canvas.height)
            ))
        }*/

        points.add(Point2D(60.0, 200.0))
        points.add(Point2D(180.0, 200.0))
        points.add(Point2D(80.0, 180.0))
        points.add(Point2D(140.0, 180.0))
        points.add(Point2D(20.0, 160.0))
        points.add(Point2D(100.0, 160.0))
        points.add(Point2D(200.0, 160.0))
        points.add(Point2D(140.0, 140.0))
        points.add(Point2D(40.0, 120.0))
        points.add(Point2D(100.0, 120.0))
        points.add(Point2D(180.0, 100.0))
        points.add(Point2D(60.0, 80.0))
        points.add(Point2D(120.0, 80.0))
        points.add(Point2D(180.0, 60.0))
        points.add(Point2D(20.0, 40.0))
        points.add(Point2D(100.0, 40.0))
        points.add(Point2D(200.0, 40.0))
        points.add(Point2D(20.0, 20.0))
        points.add(Point2D(60.0, 20.0))
        points.add(Point2D(160.0, 20.0))

        return TSPInstance(points)
    }
}
