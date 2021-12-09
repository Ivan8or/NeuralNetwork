package SimplePerceptron;

import online.umbcraft.component.Connection;
import online.umbcraft.component.ThresholdLogicUnit;
import online.umbcraft.component.activationfunction.ActivationFunction;
import online.umbcraft.component.activationfunction.HeavisideFunction;
import online.umbcraft.component.activationfunction.SigmoidFunction;
import online.umbcraft.component.activationfunction.TanhFunction;
import online.umbcraft.component.input.BiasInput;
import online.umbcraft.component.input.Input;
import online.umbcraft.component.input.PassthroughInput;
import online.umbcraft.data.DataGrapher;
import online.umbcraft.data.DataPoint;
import online.umbcraft.data.DataSet;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MultiLayerPerceptron {


    final static public Random RANDOM = new Random(420);

    public static void main(String[] args) {

        double STEP_SIZE = 0.03;
        double STEP_MULTIPLIER = 0.95;
        Scanner sc = new Scanner(System.in);

        final PassthroughInput xInput = new PassthroughInput();
        final PassthroughInput yInput = new PassthroughInput();
        //final Input biasInput = new BiasInput();
        ActivationFunction activFunc = new SigmoidFunction();

        List<Connection> conns1 = new ArrayList<>();
        final Connection xConn = new Connection(xInput);
        final Connection yConn = new Connection(yInput);
        //final Connection biasConn1 = new Connection(biasInput);
        conns1.add(xConn);
        conns1.add(yConn);
        //conns1.add(biasConn1);
        ThresholdLogicUnit node1 = new ThresholdLogicUnit(conns1, activFunc);


        List<Connection> conns2 = new ArrayList<>();
        final Connection node1Conn = new Connection(node1);
       //final Connection biasConn2 = new Connection(biasInput);
        conns2.add(node1Conn);
        //conns2.add(biasConn2);
        ThresholdLogicUnit tlu = new ThresholdLogicUnit(conns2, activFunc);


        DataSet dataset = new DataSet();

        DataGrapher datamap = new DataGrapher();
        DataGrapher failmap = new DataGrapher();

        for (int i = 0; i < 1600000; i++) {
            double x = (RANDOM.nextDouble()-0.5) * 2;
            double y = (RANDOM.nextDouble()-0.5) * 2;
            double fx = goalFunction(x);
            List<Double> features = new ArrayList<>();
            features.add(x);
            features.add(y);
            double label = (y > fx) ? 1 : 0;
            DataPoint newPoint = new DataPoint(features, label);
            dataset.add(newPoint);
        }


        DataSet[] split = dataset.splitSet(0.75);
        System.out.println("Size of training set: "+ Arrays.toString(split[0].dimensions()));
        System.out.println("Size of testing set: "+ Arrays.toString(split[1].dimensions()));


        DataSet[] trainingBatches = split[0].batches(500);

        int epoch = 0;
        for(int i = 0; i < trainingBatches.length; i++) {

            int batchSize = trainingBatches[i].getData().size();
            int trainingSuccess = 0;
            for (DataPoint point : trainingBatches[i].getData()) {
                double xcoord = point.getFeatures().get(0);
                double ycoord = point.getFeatures().get(1);
                xInput.setInput(xcoord);
                yInput.setInput(ycoord);

                double result = tlu.trainEvaluate(point.getLabel(), STEP_SIZE);


                datamap.addPoint(xcoord,ycoord,((point.getLabel() == 1)? Color.RED : Color.BLUE));

                if (point.getLabel() == Math.round(result))
                    trainingSuccess++;
                else {
                    datamap.addPoint(xcoord, ycoord + 0.01, Color.DARK_GRAY);
                    failmap.addPoint(xcoord, ycoord + 0.01, Color.DARK_GRAY);
                }
            }


            int testSuccess = 0;
            int trainingSize = split[1].getData().size();
            for (DataPoint point : split[1].getData()) {
                xInput.setInput(point.getFeatures().get(0));
                yInput.setInput(point.getFeatures().get(1));
                double result = tlu.evaluate();
                if (point.getLabel() == Math.round(result))
                    testSuccess++;
            }
            System.out.printf("%n%nbatch %d: %n",i+1);
            System.out.printf("training success rate:\t %d/%d;\t %.3f%n",
                    trainingSuccess, batchSize, (double)trainingSuccess / batchSize);
            System.out.printf("testing success rate:\t %d/%d;\t %.3f%n",
                    testSuccess, trainingSize, (double)testSuccess / trainingSize);
            System.out.println("\tweights:");
            System.out.printf("node 1: x=%.3f, y=%.3f, bias=%.3f%n",
                    xConn.getWeight(), yConn.getWeight(), 0.0/*biasConn1.getWeight()*/);
            System.out.printf("node 2: n1=%.3f, bias=%.3f%n",
                    node1Conn.getWeight(), 0.0/*biasConn2.getWeight()*/);


            STEP_SIZE *= STEP_MULTIPLIER;

            double slope = (xConn.getWeight()/yConn.getWeight());
            //double intercept = biasConn1.getWeight() / yConn.getWeight();
            double intercept = 0;
            datamap.addLine(slope, intercept, Color.MAGENTA);


            String garbage = sc.nextLine();
            if(i < trainingBatches.length-1) {
                datamap.clear();
                failmap.clear();
            }
        }
    }

    private static double goalFunction(double x) {
        return 2 * x;
    }
}
