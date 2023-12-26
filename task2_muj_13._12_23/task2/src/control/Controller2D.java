package control;

import fill.ScanLine;
import fill.SeedFill;
import model.Polygon;
import model.ResizableRectangle;
import rasterize.*;
import view.Panel;
import model.ResizableEllipse;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Controller2D implements Controller {

    private final Panel panel;
    private final Raster raster; // bez tohohle rastru mi to dělalo polygon jen od 1. zadaneho bodu

    private int x,y;
    private LineRasterizer rasterizer;
    private PolygonRasterizer polygonRasterizer;
    private LineRasterizer dottedLineRasterizer;
    private Polygon polygon;
    private SeedFill seedFill;
    ScanLine scanLineFiller;
    private ResizableRectangle rectangle;
    private ResizableEllipse resizableEllipse;

    private int mode = 1;
    private int gap = 3;



    public Controller2D(Panel panel) {
        this.panel = panel;
        this.raster = panel.getRaster();
        initObjects(panel.getRaster());
        initListeners(panel);
    }

    // Inicialazace objektů
    public void initObjects(Raster raster) {
        rasterizer = new LineRasterizeTrivial(raster);//new LineRasterizerGraphics(raster);
        polygonRasterizer = new PolygonRasterizer(raster);
        dottedLineRasterizer = new DottedLineRasterizer(raster);
        polygon = new Polygon();
        seedFill = new SeedFill(raster);
        rectangle = new ResizableRectangle(polygonRasterizer);
        resizableEllipse = new ResizableEllipse(new LineRasterizeTrivial(panel.getRaster()));

    }

    @Override
    public void initListeners(Panel panel) {
        panel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isControlDown()) return;

                if (e.isShiftDown()) {
                    if ( mode == 1 || mode == 3 ) {
                        x = e.getX();
                        y = e.getY();
                        System.out.println("Shifdole");
                    }
                    
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                //    rasterizer.rasterize(x,y,e.getX(),e.getY(),new Color(0xff0000));
                    x = e.getX();
                    y = e.getY();
                    if (mode == 2 ){
                        if(polygon.isEmpty())
                        {
                            polygon.addPoints(e.getPoint());
                        }
                    }
                    if (mode==5){
                        rectangle.setInitialCoordinates(e.getX(), e.getY());
                    }

                } else if (SwingUtilities.isMiddleMouseButton(e)) {
                    if (mode==2||mode==5){
                        seedFill.setSeed(new Point(e.getX(),e.getY()));
                        seedFill.setFillColor(0xFF0000);
                        seedFill.fill();
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    if (mode==5){
                        resizableEllipse.setInitialCoordinates(e.getX(), e.getY());
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                        if(e.getButton()==MouseEvent.BUTTON1){
                            if (mode==2){
                                raster.clear();
                                polygon.addPoints(e.getPoint());
                                update();
                        }
                }
                System.out.println("Released");
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.isControlDown()) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        //TODO
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        //TODO
                    }
                }
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //panel.clear();
                /*rasterizer.rasterize(
                        panel.getRaster().getWidth()/2,
                        panel.getRaster().getHeight()/2,
                        e.getX(),
                        e.getY(),
                        Color.YELLOW);*/

                if (e.isControlDown()) return;

                if (e.isShiftDown()) {

                    if ( mode == 1 || mode == 3 ){
                        // Lomení čáry při shiftu*********************
                        int x1_shifted = e.getX();
                        int y1_shifted = e.getY();

                        // Vzdálenost mezi body
                        int deltaX = x1_shifted - x;
                        int deltaY = y1_shifted - y;
                        double distanceBetweenPoints = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

                        // úhel lomu
                        double angle = Math.atan2(y1_shifted - y, x1_shifted - x);
                        double constrainedAngle = Math.round(angle / (Math.PI / 4)) * (Math.PI / 4);

                        int adjustedX = x + (int) (Math.cos(constrainedAngle) * distanceBetweenPoints);
                        int adjustedY = y + (int) (Math.sin(constrainedAngle) * distanceBetweenPoints);

                        if (mode == 1){
                            update();
                            rasterizer.rasterize(x, y, adjustedX, adjustedY, Color.WHITE,0);
                        } else if (mode == 3) {
                            update();
                            dottedLineRasterizer.rasterize(x, y, adjustedX, adjustedY, Color.LIGHT_GRAY,gap);
                        }

                    }

                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    if (mode == 1){
                        update();
                        rasterizer.rasterize(x, y, e.getX(), e.getY(), Color.WHITE,0);
                    }
                    if (mode == 2){
                        raster.clear();
                        update();
                        dottedLineRasterizer.rasterize(polygon.getPoint(0).x, polygon.getPoint(0).y, e.getX(), e.getY(), Color.LIGHT_GRAY,gap);
                        dottedLineRasterizer.rasterize(polygon.getPoint(polygon.getSize()-1).x, polygon.getPoint(polygon.getSize()-1).y,
                                e.getX(),e.getY(),Color.LIGHT_GRAY,gap);
                    }
                    if (mode == 3){
                        update();
                        dottedLineRasterizer.rasterize(x, y, e.getX(), e.getY(), Color.LIGHT_GRAY,gap);
                    }
                    if (mode == 5){
                        raster.clear();
                        rectangle.setCurrentCoordinates(e.getX(), e.getY());
                        rectangle.draw(Color.PINK);
                    }

                } else if (SwingUtilities.isRightMouseButton(e)) {
                    if (mode == 5){
                        raster.clear();
                        resizableEllipse.setCurrentCoordinates(e.getX(), e.getY());
                        resizableEllipse.draw(Color.PINK);
                    }

                } else if (SwingUtilities.isMiddleMouseButton(e)) {
                    //TODO
                }

            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // na klávesu C vymazat plátno
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    //TODO

                    hardClear();
                }
                // přepínání módů
                else if (e.getKeyCode() == KeyEvent.VK_U){
                    mode = 1; // úsečka
                    hardClear();
                    System.out.println("Mód změmen na: " + mode);
                }
                else if (e.getKeyCode() == KeyEvent.VK_P){
                    mode = 2; // polygon
                    hardClear();
                    System.out.println("Mód změmen na: " + mode);
                }
                else if (e.getKeyCode() == KeyEvent.VK_I) {
                    mode = 3; // Čárkovaná úsečka
                    hardClear();
                    System.out.println("Mód změmen na: " + mode);
                }
                else if (e.getKeyCode() == KeyEvent.VK_O){
                    mode = 4; // Ořezání
                    hardClear();
                    System.out.println("Mód změmen na: " + mode);
                }
                else if (e.getKeyCode() == KeyEvent.VK_L){
                    mode = 5; // Obdélník a elipsa
                    hardClear();
                    System.out.println("Mód změmen na: " + mode);
                }
                // na klávesu F vyplnit polygon metodou Scan Line (cervene)
                else if  (e.getKeyCode() == KeyEvent.VK_F) {
                    scanLineFiller = new ScanLine(raster, polygon.getPoints());
                    scanLineFiller.fill();
                    System.out.println(polygon.getPoints());
                    //polygonRasterizer.drawPolygonLines(polygon.getPoints());
                    //polygonVykreslen = true;
                }
                else if (e.getKeyCode() == KeyEvent.VK_1){
                    gap = 3;
                    System.out.println("Gap změněn na: " + gap);
                }
                else if (e.getKeyCode() == KeyEvent.VK_2){
                    gap = 6;
                    System.out.println("Gap změněn na: " + gap);
                }
                else if (e.getKeyCode() == KeyEvent.VK_3){
                    gap = 9;
                    System.out.println("Gap změněn na: " + gap);
                }
            }
        });

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.resize();
                initObjects(panel.getRaster());
            }
        });
    }

    private void update() {

        //TODO
        if ( mode == 1 || mode == 3 ){
            raster.clear();
        }

        if (mode == 2 || mode == 5){
            panel.repaint();
            polygonRasterizer.rasterize(polygon,0xff00ff);
        }

    }

    private void hardClear() {
        raster.clear();
        panel.clear();
        panel.repaint();
        polygon.clear();

    }

}
