package online.umbcraft.ml.data.jframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DataGrapher extends JFrame {

    final private PicPanel gradientPicpanel;
    final private PicPanel missesPicpanel;

    public DataGrapher() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Data Graph");
        setAlwaysOnTop(true);
        setBounds(50, 50, 535, 580);
        setLayout(null);
        getContentPane().setBackground(Color.lightGray);


        MenuListener ml = new MenuListener();
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("Display");
        JMenuItem gradient = new JMenuItem("Gradient");
        gradient.addActionListener(ml);
        menu.add(gradient);
        JMenuItem misses = new JMenuItem("Misses");
        misses.addActionListener(ml);

        menu.add(gradient);
        menu.add(misses);
        menubar.add(menu);
        setJMenuBar(menubar);


        gradientPicpanel = new PicPanel(1,1);
        gradientPicpanel.setBounds(10,10,500,500);
        this.add(gradientPicpanel);

        missesPicpanel = new PicPanel(1,1);
        missesPicpanel.setBounds(10,10,500,500);
        this.add(missesPicpanel);
        missesPicpanel.setVisible(false);
        setVisible(true);


    }

    public void addPoint_m(double x, double y, Color color) {
        missesPicpanel.addPoint(x,y,color);
    }

    public void addLine_m(double slope, double intercept, Color color) {
        missesPicpanel.addLine(slope, intercept ,color);
    }

    public void clear_m() {
        missesPicpanel.clear();
    }

    public void addPoint_g(double x, double y, Color color) {
        gradientPicpanel.addPoint(x,y,color);
    }

    public void addLine_g(double slope, double intercept, Color color) {
        gradientPicpanel.addLine(slope, intercept ,color);
    }

    public void clear_g() {
        gradientPicpanel.clear();
    }

    private class MenuListener implements ActionListener {

        public void actionPerformed(ActionEvent e)
        {
            if("Gradient".equals(e.getActionCommand())){
                missesPicpanel.setVisible(false);
                gradientPicpanel.setVisible(true);
            }
            if("Misses".equals(e.getActionCommand())){
                gradientPicpanel.setVisible(false);
                missesPicpanel.setVisible(true);
            }
        }
    }

}
