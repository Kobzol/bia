package app.gui

import javafx.embed.swing.SwingFXUtils
import javafx.scene.Node
import javafx.scene.SnapshotParameters
import javafx.scene.image.WritableImage
import java.awt.Component
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

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
