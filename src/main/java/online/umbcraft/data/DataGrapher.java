package online.umbcraft.data;

import online.umbcraft.data.jframe.PicPanel;

import javax.swing.*;
import java.awt.*;

public class DataGrapher extends JFrame {

    final private PicPanel picpanel;

    public DataGrapher() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Data Graph");
        setAlwaysOnTop(true);
        setBounds(50, 50, 510, 530);
        setLayout(null);
        getContentPane().setBackground(Color.lightGray);


        picpanel = new PicPanel(1,1);
        picpanel.setBounds(0,0,500,500);
        this.add(picpanel);
        setVisible(true);
    }

    public void addPoint(double x, double y, Color color) {
        picpanel.addPoint(x,y,color);
    }
    public void addLine(double slope, double intercept, Color color) {
        picpanel.addLine(slope, intercept ,color);
    }
    public void clear() {
        picpanel.clear();
    }

}
