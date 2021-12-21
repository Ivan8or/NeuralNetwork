package SimplePerceptron;

import online.umbcraft.data.inputs.DataPoint;
import online.umbcraft.data.inputs.DataSet;
import online.umbcraft.data.jframe.DataGrapher;
import online.umbcraft.ml.perceptron.Perceptron;

import java.awt.*;
import java.io.File;
import java.util.Scanner;

public class ImportPerceptron {

    public static void main(String[] args) throws Exception {

        File importFile = new File("net.json");
        Perceptron perceptron = new Perceptron(importFile);


        DataSet dataset = new DataSet();
        DataGrapher graph = new DataGrapher(dataset, 2);

        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        Color[] colors = {Color.BLUE, Color.RED, Color.BLACK, Color.CYAN};

        double interval = 0.3;
        //double interval = 0.1575;
        for (double x = -10.0; x < 10; x+=interval) {
            for (double y = -10.0; y < 10; y+=interval) {
                DataPoint newPoint = new DataPoint(
                        new double[]{x, y});
                dataset.add(newPoint);
            }
        }

        for (DataPoint testPoint : dataset.getData()) {
            perceptron.setFeatures(testPoint);
            double[] guesses = perceptron.evaluate(testPoint.getFeatures());
            int largestIndex = largestIndex(guesses);
            graph.addPoint(0,
                    testPoint.getFeatures()[0],
                    testPoint.getFeatures()[1],
                    largestIndex
            );

        }
    }

    public static int largestIndex(double[] arr) {
        int maxIndex = 0;
        double maxVal = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > maxVal) {
                maxIndex = i;
                maxVal = arr[i];
            }
        }
        return maxIndex;
    }
}
