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

public class SinglePerceptron {

    final static public Random RANDOM = new Random(420);

    public static void main(String[] args) {

        double STEP_SIZE = 0.5;
        double STEP_MULTIPLIER = 0.75;

        final PassthroughInput xInput = new PassthroughInput();
        final PassthroughInput yInput = new PassthroughInput();
        Scanner sc = new Scanner(System.in);
        final Input biasInput = new BiasInput();

        List<Connection> conns = new ArrayList<>();

        final Connection xConn = new Connection(xInput);
        final Connection yConn = new Connection(yInput);
        final Connection biasConn = new Connection(biasInput);
        conns.add(xConn);
        conns.add(yConn);
        conns.add(biasConn);

        ActivationFunction activFunc = new SigmoidFunction();

        ThresholdLogicUnit tlu = new ThresholdLogicUnit(conns, activFunc);

        DataSet dataset = new DataSet();

        DataGrapher graph = new DataGrapher();

        for (int i = 0; i < 150000; i++) {
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


        DataSet[] split = dataset.splitSet(0.9875);
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


                graph.addPoint(xcoord,ycoord,((point.getLabel() == 1)? Color.RED : Color.BLUE));

                if (point.getLabel() == Math.round(result))
                    trainingSuccess++;
                else
                    graph.addPoint(xcoord,ycoord+0.01,Color.DARK_GRAY);
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
            System.out.printf("batch %d success rate: %d/%d; %.3f%n",
                    i+1, trainingSuccess, batchSize, (double)trainingSuccess / batchSize);
            System.out.printf("batch %d weights: x=%.3f, y=%.3f, bias=%.3f%n",
                    i+1, xConn.getWeight(), yConn.getWeight(), biasConn.getWeight());
            System.out.printf("training %d success rate: %d/%d; %.3f%n%n",
                    i+1, testSuccess, trainingSize, (double)testSuccess / trainingSize);

            STEP_SIZE *= STEP_MULTIPLIER;

            double slope = (xConn.getWeight()/yConn.getWeight());
            double intercept = biasConn.getWeight() / yConn.getWeight();
            graph.addLine(slope, intercept, Color.MAGENTA);


            String garbage = sc.nextLine();
            if(i < trainingBatches.length-1)
                graph.clear();
        }
    }

    private static double goalFunction(double x) {
        return 2*x - 0.5;
    }
}
