package app.gui

import java.awt.Component
import java.io.IOException
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.io.File

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
