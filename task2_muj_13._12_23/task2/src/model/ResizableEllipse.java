package model;
import java.awt.Color;
import model.Polygon;
import rasterize.LineRasterizer;

public class ResizableEllipse {
    private final LineRasterizer lineRasterizer;
    private int initialX, initialY, currentX, currentY;

    public ResizableEllipse(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public void setInitialCoordinates(int x, int y) {
        initialX = x;
        initialY = y;
    }

    public void setCurrentCoordinates(int x, int y) {
        currentX = x;
        currentY = y;
    }

    public void draw(Color color) {
        int centerX = (initialX + currentX) / 2;
        int centerY = (initialY + currentY) / 2;
        int radiusX = Math.abs(initialX - currentX) / 2;
        int radiusY = Math.abs(initialY - currentY) / 2;

        drawEllipse(centerX, centerY, radiusX, radiusY, color);
    }

    private void drawEllipse(int centerX, int centerY, int radiusX, int radiusY, Color color) {
        double angle = 0;
        double angleStep = 0.01; // proměnná určující detailing

        int x1 = (int) (centerX + radiusX * Math.cos(angle));
        int y1 = (int) (centerY + radiusY * Math.sin(angle));

        while (angle < Math.PI * 2) {
            angle += angleStep;
            int x2 = (int) (centerX + radiusX * Math.cos(angle));
            int y2 = (int) (centerY + radiusY * Math.sin(angle));

            lineRasterizer.rasterize(x1, y1, x2, y2, color, 1);
            x1 = x2;
            y1 = y2;
        }
    }
}