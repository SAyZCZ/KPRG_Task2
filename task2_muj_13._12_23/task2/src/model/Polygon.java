package model;

import java.awt.*;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Polygon {

	//TODO

    private final List<java.awt.Point> points;
    private final int color;

    public Polygon() {
        this(new ArrayList<>());
    }

    public Polygon(List<java.awt.Point> points) {
        this(points, Color.BLUE.getRGB());
    }

    public Polygon(List<java.awt.Point> points, int color) {
        this.points = points;
        this.color = color;
    }

    public void addPoints(java.awt.Point... pointsToAdd) {
        points.addAll(Arrays.asList(pointsToAdd));
    }

    public void addPoints(List<java.awt.Point> pointsToAdd) {
        points.addAll(pointsToAdd);
    }

    public java.awt.Point getPoint(int index)
    {
        return points.get(index);
    }

    public void clear(){points.clear();}

    public int getSize()
    {
        return points.size();
    }

    public boolean isEmpty()
    {
        return points.isEmpty();
    }

    public List<Point> getPoints()
    {
        return points;
    }

    private final List<Line> polygonHranice = new ArrayList<>();

    public List<Line> polygonHranice(List<model.Point> polygonPoints) {
        if (polygonPoints.size() > 2) {
            model.Point temporaryPoint = new model.Point();
            model.Point firstPoint = new model.Point();
            int listIndex = 0;
            for (model.Point point : polygonPoints) {
                listIndex++;
                if (listIndex == 1) {
                    firstPoint = new model.Point(point.getX(), point.getY());

                } else {
                    polygonHranice.add(new Line(temporaryPoint, point, Color.GREEN.getRGB()));
                    if (listIndex == polygonPoints.size()) {
                        polygonHranice.add(new Line(point, firstPoint, Color.GREEN.getRGB()));
                    }
                }
                temporaryPoint = new model.Point(point.getX(), point.getY());
            }

        } else {
            System.out.println("NELZE PROVEST");
        }
        return polygonHranice;
    }

}



