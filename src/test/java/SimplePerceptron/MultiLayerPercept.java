package SimplePerceptron;

import online.umbcraft.data.inputs.DataPoint;
import online.umbcraft.data.inputs.DataSet;
import online.umbcraft.data.jframe.DataGrapher;
import online.umbcraft.ml.functions.activations.TanhFunction;
import online.umbcraft.ml.functions.costs.MeanSquaredError;
import online.umbcraft.ml.perceptron.Perceptron;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MultiLayerPercept {

    final static public Random RANDOM = new Random(420);

    public static int goalFunction(double x, double y) {

        double toCenter = Math.sqrt(2*x*x + y*y);
        if(toCenter < 3)
            return 0;
        if(toCenter < 6.5)
            return 1;
        if(toCenter < 11.5)
            return 2;
        return 3;
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

    public static void main(String[] args) throws IOException {
        Perceptron perceptron = new Perceptron(
                new TanhFunction(),
                new MeanSquaredError(),
                2, 8, 8, 4
        );

        double STEP_SIZE = 0.2;

        DataSet dataset = new DataSet();
        DataGrapher graph = new DataGrapher(dataset, 2);

        // dataset of size 10k

//        double interval = 0.3;
//        //double interval = 0.1575;
//        for (double x = -10.0; x < 10; x+=interval) {
//            for (double y = -10.0; y < 10; y+=interval) {
//
//                // function to imitate
//                //boolean fx = (x*x +y*y < 1.5*1.5);
//                double[] labels = {-1, -1, -1, -1};
//
//                int goalIndex = goalFunction(x, y);
//                labels[goalIndex] = 1;
//
//                DataPoint newPoint = new DataPoint(
//                        new double[]{x, y},
//                        labels);
//                dataset.add(newPoint);
//                graph.addPoint(0,x, y, colors[goalIndex]);
//            }
//        }

        Scanner sc = new Scanner(System.in);
        System.out.println("manual point entry...");
        sc.nextLine();
        dataset = graph.getDataset();
        System.out.println("dataset size: "+dataset.dimensions()[1]);

        dataset.shuffle();
        dataset = dataset.splitSet(0.05)[0];
        DataSet[] batches = dataset.batches(4);


        int epoch = 0;
        for (int i = 0; i < 1000; i++) {
            System.out.println("----Epoch " + epoch+":");
            int iteration = 0;
            for (DataSet batch : batches) {
                System.out.println("----Iteration " + iteration+":");
                iteration++;
                double totalError = 0;
                int numRight = 0;
                double testSize = dataset.getData().size();

                double startTestTime = System.currentTimeMillis();

                for (DataPoint testPoint : dataset.getData()) {
                    perceptron.setFeatures(testPoint);
                    double[] guesses = perceptron.evaluate(testPoint.getFeatures());
                    int largestIndex = largestIndex(guesses);
                    graph.addPoint(1,
                            testPoint.getFeatures()[0],
                            testPoint.getFeatures()[1],
                            largestIndex
                    );
                    if(testPoint.getLabels()[largestIndex] == 1)
                        numRight++;

                    // calculate error for all labels for the output
                    for (int labelIndex = 0; labelIndex < testPoint.getLabels().length; labelIndex++) {
                        double label = testPoint.getLabels()[labelIndex];
                        double guess = guesses[labelIndex];
                        totalError += perceptron.getErrorFunction().result(guess, label);
                    }
                }
                double endTestTime = System.currentTimeMillis();
                double avgError = totalError / (testSize * dataset.getData().get(0).getLabels().length);

                // train
                double startTime = System.currentTimeMillis();
                perceptron.trainIteration(batch, STEP_SIZE);
                double endTime = System.currentTimeMillis();
                System.out.println("testing iteration took "+(endTestTime-startTestTime)+"ms");
                System.out.println("training iteration took "+(endTime-startTime)+"ms");
                System.out.println("EF error is " + avgError);
                System.out.println("Percentage is " + (numRight/testSize));

            }
            STEP_SIZE *= 0.99;
            epoch++;


            File outputFile = new File("net.json");
            perceptron.export(outputFile);


            System.out.println("JSON:\n"+perceptron.asJSON().toJSONString());
        }


    }
}
