package model;

import java.awt.Color;
import java.awt.Point;
import model.Polygon;
import rasterize.PolygonRasterizer;

public class ResizableRectangle {
    private final PolygonRasterizer polygonRasterizer;
    private int x, y, currentX, currentY;

    public void setInitialCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setCurrentCoordinates(int currentX, int currentY) {
        this.currentX = currentX;
        this.currentY = currentY;
    }
    public ResizableRectangle( PolygonRasterizer polygonRasterizer) {
        this.polygonRasterizer = polygonRasterizer;
    }

    public void draw(Color color) {


        Polygon rectangle = new Polygon();
        rectangle.addPoints(new Point(x, y));
        rectangle.addPoints(new Point(currentX, y));
        rectangle.addPoints(new Point(currentX, currentY));
        rectangle.addPoints(new Point(x, currentY));

        polygonRasterizer.rasterize(rectangle, color.getRGB());
    }
}
