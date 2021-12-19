package online.umbcraft.data.jframe;

import online.umbcraft.data.inputs.DataPoint;
import online.umbcraft.data.inputs.DataSet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DataGrapher extends JFrame {

    final private PicPanel gradientPicpanel;
    final private PicPanel missesPicpanel;
    final private DataSet dataset;


    public DataGrapher(DataSet dataset) {

        this.dataset = dataset;

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
        gradientPicpanel.addMouseListener(new ClickListener(this));
        this.add(gradientPicpanel);

        missesPicpanel = new PicPanel(1,1);
        missesPicpanel.setBounds(10,10,500,500);
        this.add(missesPicpanel);
        missesPicpanel.addMouseListener(new ClickListener(this));
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


    private class ClickListener implements MouseListener {

        final private DataGrapher dg;


        public ClickListener(DataGrapher dg) {
            this.dg = dg;
        }

        @Override
        public void mouseClicked(MouseEvent e) {


        }

        @Override
        public void mousePressed(MouseEvent e) {

            double x = e.getPoint().getX();
            double y = e.getPoint().getY();
            System.out.println("CLICKED!! x=" + x + " y=" + y);

            for(int i = 0; i < 20; i++) {

                double[] coords = gradientPicpanel.descaleCoord(
                        x + (Math.random()-0.5) * 50,
                        y + (Math.random()-0.5) * 50);

                System.out.println("graphing point at x=" + coords[0] + " y=" + coords[1]);

                boolean blue = e.getButton() == MouseEvent.BUTTON1;

                addPoint_g(coords[0], coords[1], (blue) ? Color.BLUE : Color.RED);

                dataset.add(new DataPoint(coords, new double[]{((blue) ? -1.0 : 1.0)}));
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
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
