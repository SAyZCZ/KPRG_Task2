package rasterize;

import model.Line;

import java.awt.*;

public abstract class LineRasterizer {
    Raster raster;
    Color color;

    public LineRasterizer(Raster raster){
        this.raster = raster;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setColor(int color) {
        this.color = new Color(color);
    }

    // pro použití v polygon rasterizeru
    public void rasterize(Line line) {
        //TODO
        setColor(line.getColor());
        rasterize(line.get_X1(), line.get_Y1(), line.get_X2(), line.get_Y2(), color,0);
    }

    public void rasterize(int x1, int y1, int x2, int y2, Color color,int gap) {
        //TODO
    }

    protected void drawLine(int x1, int y1, int x2, int y2) {
    // tuhle metodu má iplmenetovat až potomek takže nevadí že je prázdno
    }

}
