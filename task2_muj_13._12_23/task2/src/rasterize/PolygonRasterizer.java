package rasterize;

import model.Line;
import model.Polygon;

import java.awt.*;
import java.util.List;

public class PolygonRasterizer {

    //TODO
    LineRasterizer lineRasterizer;

    public PolygonRasterizer(Raster raster)
    {
        lineRasterizer = new LineRasterizeTrivial(raster);
    }

    public void rasterize(Polygon polygon,int color)
    {
        for(int i = 1; i <= polygon.getSize();++i)
        {
            java.awt.Point start = polygon.getPoint(i-1);//polygon.getPoint(i-1);
            Point end = polygon.getPoint(i % polygon.getSize());

            // cast double do intu
            int startX = (int) start.getX();
            int startY = (int) start.getY();
            int endX = (int) end.getX();
            int endY = (int) end.getY();

            lineRasterizer.rasterize(startX,startY,endX,endY,Color.PINK,0);
        }
    }

}
