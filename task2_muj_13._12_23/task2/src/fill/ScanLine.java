package fill;
import model.Line;
import model.Polygon;
import rasterize.LineRasterizeTrivial;
import rasterize.Raster;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import model.Point;
public class ScanLine implements Filler {

    private final Raster raster;
    private final List<model.Point> vrcholyPolygonu;
    private List<Line> usecky;
    private Color fillColor = Color.RED;
    private final Polygon polygonToFill;
    private List<Point> pruseciky;
    int ymax = 0;
    int ymin;


    public ScanLine(Raster raster, List<java.awt.Point> awtPoints) {
        this.raster = raster;

        // Convert java.awt.Point objects to model.Point objects
        List<model.Point> modelPoints = new ArrayList<>();
        for (java.awt.Point awtPoint : awtPoints) {
            modelPoints.add(new model.Point(awtPoint.x, awtPoint.y)); // Assuming x and y are coordinates of awtPoint
        }

        this.vrcholyPolygonu = modelPoints;

        // Create a new list of java.awt.Point to pass into the Polygon constructor
        List<java.awt.Point> awtPolygonPoints = new ArrayList<>();
        for (model.Point p : modelPoints) {
            awtPolygonPoints.add(new java.awt.Point(p.getX(), p.getY())); // Assuming getX and getY methods in model.Point
        }

        this.polygonToFill = new Polygon(awtPolygonPoints);
        ymin = raster.getHeight();
    }


    @Override
    public void fill() {
        fillPolygon();
    }

    private void fillPolygon() {
        usecky = prepareLines(polygonToFill.polygonHranice(vrcholyPolygonu));
        getPruseciky(usecky);
        fillHorizontal(3);
    }


    private List<Line> prepareLines(List<Line> lineList) {
        List<Line> preparedLines = new ArrayList<>();
        for (Line line : lineList) {
            if (line.get_Y1() != line.get_Y2()) {                 // Neberu vodorovne hrany
                if (line.get_Y2() > line.get_Y1()) {              // Smerovani usecek dolu (Y2 > Y1)
                    preparedLines.add(new Line(line.get_X1(), line.get_Y1(), line.get_X2(), line.get_Y2() - 1, fillColor.getRGB()));
                } else {
                    preparedLines.add(new Line(line.get_X2(), line.get_Y2(), line.get_X1(), line.get_Y1() - 1, fillColor.getRGB()));
                }
                if (preparedLines.get(preparedLines.size() - 1).get_Y2() > ymax) {
                    ymax = preparedLines.get(preparedLines.size() - 1).get_Y2();
                }
                if (preparedLines.get(preparedLines.size() - 1).get_Y1() < ymin) {
                    ymin = preparedLines.get(preparedLines.size() - 1).get_Y1();
                }
            }
        }
        return preparedLines;
    }

    private List<Point> getPruseciky(List<Line> usecky) {
        pruseciky = new ArrayList<>();

        for (Line usecka : usecky) {
            int yStart = usecka.get_Y1();
            int yEnd = usecka.get_Y2();

            if (yStart > yEnd) {
                int temp = yStart;
                yStart = yEnd;
                yEnd = temp;
            }

            float slope = (usecka.get_X2() - usecka.get_X1()) / (float) (usecka.get_Y2() - usecka.get_Y1());

            for (int i = yStart; i <= yEnd; i++) {
                if (i >= 0 && i < raster.getHeight()) {
                    int xIntersect = Math.round(usecka.get_X1() + slope * (i - usecka.get_Y1()));
                    pruseciky.add(new Point(xIntersect, i));
                }
            }
        }

        pruseciky.sort(Comparator.comparingInt(Point::getY).thenComparingInt(Point::getX));
        return pruseciky;
    }

    private void fillHorizontal(int srafa) {
        for (int i = ymin; i <= ymax; i += srafa) {
            boolean inside = false;
            int xStart = 0;

            for (Point p : pruseciky) {
                if (p.getY() == i) {
                    if (!inside) {
                        xStart = p.getX();
                        inside = true;
                    } else {
                        LineRasterizeTrivial horizontalRasterizer = new LineRasterizeTrivial(raster);
                        horizontalRasterizer.rasterize(xStart, i, p.getX(), i, fillColor, 0);
                        inside = false;
                    }
                }
            }
        }
    }
}
