package app.gui

import algorithm.Algorithm
import algorithm.FunctionFitness
import javafx.embed.swing.SwingFXUtils
import javafx.scene.Node
import javafx.scene.SnapshotParameters
import javafx.scene.image.WritableImage
import java.awt.Component
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.math.abs

fun renderComponent(component: Component, path: String)
{
    val img = BufferedImage(component.width, component.height, BufferedImage.TYPE_INT_ARGB)
    val g = img.graphics
    component.paint(g)
    g.dispose()

    try
    {
        ImageIO.write(img, "png", File(path))
    }
    catch (e: IOException)
    {
        e.printStackTrace()
    }
}

fun renderComponentFX(node: Node, path: String)
{
    val image = WritableImage(600, 600)
    val snapshot = node.snapshot(SnapshotParameters(), image)
    val output = File(path)
    ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", output)
}

fun profile(createAlgorithm: () -> Algorithm, iterations: Int, targetFitness: Float)
{
    val count = 10
    var sum = 0.0
    var calcSum = 0
    var zeroSum = 0
    var firstToZeroSum = 0

    for (i in 0 until count)
    {
        val start = System.nanoTime()

        FunctionFitness.COUNTER = 0
        val algorithm = createAlgorithm()

        var added = false
        for (iter in 0 until iterations)
        {
            algorithm.runIteration()

            if (!added && algorithm.population.find { abs(it.fitness!! - targetFitness) < 0.1f } != null)
            {
                firstToZeroSum += iter + 1
                added = true
            }
        }

        if (!added)
        {
            firstToZeroSum = iterations
        }

        val end = System.nanoTime()

        zeroSum += algorithm.population.sortedDescending().takeWhile { abs(it.fitness!! - targetFitness) < 1e-2 }.size
        calcSum += FunctionFitness.COUNTER

        val time = (end - start) / 1000000.0
        System.out.println("Time: $time ms")
        sum += time
    }

    System.out.println("Avg time: ${sum / count}, avg calc: ${calcSum / count}, " +
            "avg zero results: ${zeroSum / count}, avg iter to zero: ${firstToZeroSum / count}")
}
