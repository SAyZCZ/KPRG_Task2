package fill;

import rasterize.Raster;

import java.awt.*;

public class SeedFill implements Filler {

    private final Raster raster;
    private int seedX, seedY;
    private int backgroundColor;
    private int fillColor;

    public SeedFill(Raster raster) {
        this.raster = raster;
    }

    // nastavení seedu
    public void setSeed(Point seed) {
        seedX = seed.x;
        seedY = seed.y;
        backgroundColor = raster.getPixel(seedX, seedY);
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    @Override
    public void fill() {
        seed(seedX, seedY);

    }

    private void seed(int x, int y) {
        // podmínka na porovnání barvy pixelu a backgroundu
        if (backgroundColor == raster.getPixel(x,y)) {
            raster.setPixel(x, y, fillColor);
            seed(x + 1, y);   // rekurzivní podívání se na všechny strany
            seed(x - 1,y);
            seed(x, y + 1);
            seed(x, y - 1);
        }
    }
}
