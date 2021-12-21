package online.umbcraft.data.jframe;

import online.umbcraft.data.inputs.DataPoint;
import online.umbcraft.data.inputs.DataSet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataGrapher extends JFrame {

    final private List<PicPanel> panels;
    final private Color[] colors = {
            Color.BLUE,
            Color.RED,
            Color.YELLOW,
            Color.CYAN,
            Color.GREEN,
            Color.MAGENTA,
            Color.ORANGE,
            Color.PINK,
            Color.BLACK,
    };

    private DataSet dataset;

    public DataGrapher(DataSet dataset, int numPanels) {

        this.dataset = dataset;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Data Graph");
        setAlwaysOnTop(true);
        setBounds(50, 50, 535, 580);
        setLayout(null);
        getContentPane().setBackground(Color.lightGray);

        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("Display");
        MenuListener listener = new MenuListener(this);

        for (int i = 1; i <= numPanels; i++) {
            JMenuItem panel = new JMenuItem("Panel " + i);
            panel.addActionListener(listener);
            panel.setName((i - 1) + "");
            menu.add(panel);
        }
        //menu.addActionListener();
        menubar.add(menu);
        setJMenuBar(menubar);

        panels = new ArrayList<>();

        double xRadius = 10;
        double yRadius = 10;

        for (int i = 0; i < numPanels; i++) {
            PicPanel panel = new PicPanel(xRadius, yRadius);
            panel.setBounds(10, 10, 500, 500);
            panels.add(panel);
            panel.addMouseListener(new ClickListener(i, this));
            panel.setVisible(false);

            this.add(panel);
        }

        setVisible(true);
    }

    public DataSet getDataset() {
        return dataset;
    }

    public void setDataset(DataSet newSet) {
        this.dataset = newSet;
    }

    public List<PicPanel> getPanels() {
        return panels;
    }

    public PicPanel getPanel(int panelIndex) {
        return panels.get(panelIndex);
    }

    public void addPoint(int panel, double x, double y, int color) {
        panels.get(panel).addPoint(x, y, colors[color]);
    }

    public void addLine(int panel, double slope, double intercept, int color) {
        panels.get(panel).addLine(slope, intercept, colors[color]);
    }

    public void clear(int panel) {
        panels.get(panel).clear();
    }

    private class ClickListener implements MouseListener {

        final private DataGrapher dg;
        final private int panelID;
        final private int[][] pixels;

        private int[] startCoord;

        public ClickListener(int panelID, DataGrapher dg) {
            this.dg = dg;
            this.panelID = panelID;

            int width = dg.getPanel(panelID).getWidth();
            int height = dg.getPanel(panelID).getHeight();

            pixels = new int[width][height];

            for(int i = 0; i < width; i++) {
                for(int j = 0; j < height; j++) {
                    pixels[i][j] = -1;
                }
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }


        @Override
        public void mousePressed(MouseEvent e) {
            int x = (int) e.getPoint().getX();
            int y = (int) e.getPoint().getY();
            startCoord = new int[]{x, y};
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            int x = (int) e.getPoint().getX();
            int y = (int) e.getPoint().getY();
            int[] endCoord = new int[]{x, y};

            System.out.println("Started on" + Arrays.toString(startCoord));
            System.out.println("Ended on" + Arrays.toString(endCoord));

            int smallX = Math.min(startCoord[0], endCoord[0]);
            int largeX = Math.max(startCoord[0], endCoord[0]);

            int smallY = Math.min(startCoord[1], endCoord[1]);
            int largeY = Math.max(startCoord[1], endCoord[1]);

            int colorIndex = switch (e.getButton()) {
                case MouseEvent.BUTTON1 -> 0;
                case MouseEvent.BUTTON2 -> 1;
                case MouseEvent.BUTTON3 -> 2;
                default -> 3;
            };

            for (int i = smallX; i < largeX; i++) {
                for (int j = smallY; j < largeY; j++) {
                    pixels[i][j] = colorIndex;
                    double[] coords = dg.getPanel(panelID).descaleCoord(i, j);
                    dg.addPoint(panelID, coords[0], coords[1], colorIndex);
                }
            }

            dataset = new DataSet();
            for(int i = 0; i < pixels.length; i++){
                for(int j = 0; j < pixels[0].length; j++) {
                    if(pixels[i][j] != -1) {
                        double[] coords = dg.getPanel(panelID).descaleCoord(i, j);
                        double[] labels = {-1, -1, -1, -1};
                        labels[pixels[i][j]] = 1;
                        dataset.add(new DataPoint(coords, labels));
                    }
                }
            }
        }


        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    private class MenuListener implements ActionListener {

        final private DataGrapher dg;

        public MenuListener(DataGrapher dg) {
            this.dg = dg;
        }

        public void actionPerformed(ActionEvent e) {
            JMenuItem menuitem = (JMenuItem) e.getSource();
            int id = Integer.parseInt(menuitem.getName());

            for (PicPanel p : dg.getPanels()) {
                p.setVisible(false);
            }
            dg.getPanel(id).setVisible(true);

        }
    }

}
