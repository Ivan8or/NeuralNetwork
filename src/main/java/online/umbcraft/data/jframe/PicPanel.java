package online.umbcraft.data.jframe;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PicPanel extends JPanel {
    private int width = 500;
    private int height = 500;

    final private double xRadius;
    final private double yRadius;
    private BufferedImage img = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);

    public PicPanel(double xRadius, double yRadius)
    {
        super();
        this.xRadius = xRadius;
        this.yRadius = yRadius;
        clear();
    }

    public double[] descaleCoord(double pixX, double pixY) {
        double xRatio = pixX / width;
        double coordX = (xRatio - 0.5) * xRadius*2;//(int)((width/2) * xRatio);

        double yRatio = pixY / height;
        double coordY = (0.5 - yRatio) * yRadius*2; // (height/2) + scaledy;

        return new double[]{coordX,coordY};
    }

    public int[] scaleCoord(double rawX, double rawY) {
        double xRatio = rawX / xRadius;
        int scaledx = (int)((width/2) * xRatio);
        int x = (width/2) + scaledx;

        double yRatio = rawY / yRadius;
        int scaledy = (int)((height/2) * yRatio);
        int y = (height/2) + scaledy;

        return new int[]{x,y};
    }

    public void clear() {
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0,0,width,height);

        g.setColor(Color.BLACK);
        g.drawLine(0,height/2, width,height/2);
        g.drawLine(width/2,0,width/2, height);
        repaint();
    }
    public void addLine(double slope, double intercept, Color color) {

        Graphics2D g = img.createGraphics();
        g.setColor(color);

        double y1 = -1 * slope + intercept;
        double y2 = 1 * slope + intercept;

        int[] coord1 = scaleCoord(-1, y1);
        int[] coord2 = scaleCoord(1, y2);

        g.drawLine(coord1[0], coord1[1], coord2[0], coord2[1]);
        repaint();
    }
    public void addPoint(double rawX, double rawY, Color color) {

        int[] coords = scaleCoord(rawX, rawY);


        Graphics2D g = img.createGraphics();
        g.setColor(color);
        g.fillOval(coords[0],height - coords[1],5,5);
        repaint();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponents(g);
        if ((width <= 0) || (height <= 0))
        {
            width = img.getWidth(this);
            height = img.getHeight(this);
        }
        g.drawImage(img,0,0,width,height,this);
    }
}