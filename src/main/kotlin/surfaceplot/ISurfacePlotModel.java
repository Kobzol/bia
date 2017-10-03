package surfaceplot;

// https://sourceforge.net/p/javasurfaceplot/code/HEAD/tree/trunk/src/net/sf/surfaceplot/

import java.awt.*;

/**
 *
 * @author salagarsamy
 */
public interface ISurfacePlotModel
{
    int PLOT_MODE_WIREFRAME = 0;
    int PLOT_MODE_NORENDER = 1;
    int PLOT_MODE_SPECTRUM = 2;
    int PLOT_MODE_GRAYSCALE = 3;
    int PLOT_MODE_DUALSHADE = 4;

    int getPlotMode();

    float calculateZ(float x, float y);
    Point3D[] getExtraVertices();
    Color getExtraColor();

    boolean isBoxed();

    boolean isMesh();

    boolean isScaleBox();

    boolean isDisplayXY();

    boolean isDisplayZ();

    boolean isDisplayGrids();

    int getCalcDivisions();
    int getDispDivisions();

    float getXMin();
    float getXMax();

    float getYMin();
    float getYMax();

    float getZMin();
    float getZMax();

    String getXAxisLabel();
    String getYAxisLabel();
    String getZAxisLabel();
}
