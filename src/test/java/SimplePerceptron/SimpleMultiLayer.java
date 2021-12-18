package SimplePerceptron;

import online.umbcraft.ml.component.Connection;
import online.umbcraft.ml.component.ThresholdLogicUnit;
import online.umbcraft.ml.component.activationfunction.ActivationFunction;
import online.umbcraft.ml.component.activationfunction.SigmoidFunction;
import online.umbcraft.ml.component.activationfunction.TanhFunction;
import online.umbcraft.ml.component.costfunction.MeanSquaredCost;
import online.umbcraft.ml.component.input.BiasInput;
import online.umbcraft.ml.component.input.Input;
import online.umbcraft.ml.component.input.PassthroughInput;
import online.umbcraft.ml.component.input.TLUInput;
import online.umbcraft.ml.data.DataPoint;
import online.umbcraft.ml.data.DataSet;
import online.umbcraft.ml.data.jframe.DataGrapher;

import java.awt.*;
import java.util.List;
import java.util.*;

public class SimpleMultiLayer {

    final static public Random RANDOM = new Random(420);

    private static double goalFunction(double x, double y) {
        return (x*x + y*y < 0.75*0.75) ? 1 : -1;
    }

    private static double mean(double[] arr) {
        double total = 0;
        for(double d: arr) {
            total += d;
        }
        return total / arr.length;
    }

    public static void main(String[] args) throws InterruptedException {

        Scanner sc = new Scanner(System.in);

        double STEP_SIZE = 0.35;
        double STEP_MULTIPLIER = 0.95;

        ActivationFunction activationFunction = new TanhFunction();

        // layer 0
        final PassthroughInput xInput = new PassthroughInput();
        final PassthroughInput yInput = new PassthroughInput();

        // layer 1
        ThresholdLogicUnit layer11node = new ThresholdLogicUnit(
                List.of(new Connection(xInput),
                        new Connection(yInput),
                        new Connection(new BiasInput())),
                activationFunction);

        ThresholdLogicUnit layer12node = new ThresholdLogicUnit(
                List.of(new Connection(xInput),
                        new Connection(yInput),
                        new Connection(new BiasInput())),
                activationFunction);

        // layer 2
        ThresholdLogicUnit layer2node = new ThresholdLogicUnit(
                List.of(new Connection(new TLUInput(layer11node)),
                        new Connection(new TLUInput(layer12node)),
                        new Connection(new BiasInput())),
                activationFunction);


        DataSet dataset = new DataSet();
        DataGrapher graph = new DataGrapher();


        // generate data
        for (int i = 0; i < 45000; i++) {
            double x = (RANDOM.nextDouble() - 0.5) * 2;
            double y = (RANDOM.nextDouble() - 0.5) * 2;
            double fx = goalFunction(x,y);
            List<Double> features = new ArrayList<>();
            features.add(x);
            features.add(y);
            double label = (fx > 0) ? 1 : -1;
            DataPoint newPoint = new DataPoint(features, label);
            dataset.add(newPoint);
            graph.addPoint_g(x,y,(label == 1)?Color.RED : Color.BLUE);
        }
        DataSet[] split = dataset.splitSet(0.5);
        //DataSet[] trainingBatches = split[0].batches(15);

        System.out.println("Size of testing set: \t" + Arrays.toString(split[1].dimensions()));
        System.out.println("Size of training set: \t" + Arrays.toString(split[0].dimensions()));
        //System.out.println("Size of each batch: \t"+ Arrays.toString(trainingBatches[0].dimensions()));

        MeanSquaredCost mse = new MeanSquaredCost();

        Thread.sleep(3000);
        for (int epoch = 1; epoch <= 1000; epoch++) {

            Thread.sleep(10);

            double totalError = 0;
            double testSize = split[1].getData().size();

            for(DataPoint testPoint : split[1].getData()) {
                xInput.setInput(testPoint.getFeatures().get(0));
                yInput.setInput(testPoint.getFeatures().get(1));
                double guess = layer2node.evaluate();
                graph.addPoint_m(
                        testPoint.getFeatures().get(0),
                        testPoint.getFeatures().get(1),
                        (guess > 0)?Color.RED: Color.BLUE
                        );

                double cost = mse.result(guess, testPoint.getLabel());
                totalError += cost;
            }
            double avgError = totalError / testSize;
            System.out.println("Epoch "+epoch+": error is "+avgError);
            //sc.nextLine();

            int size = split[0].getData().size();

            double[] weight21Adjust = new double[size];
            double[] weight22Adjust = new double[size];
            double[] bias21Adjust = new double[size];
            double[] node21Adjust = new double[size];
            double[] node22Adjust = new double[size];

            double[] xWeight1Adjust = new double[size];
            double[] yWeight1Adjust = new double[size];
            double[] bias11Adjust = new double[size];

            double[] xWeight2Adjust = new double[size];
            double[] yWeight2Adjust = new double[size];
            double[] bias12Adjust = new double[size];

            for (int trainIndex = 0; trainIndex < size; trainIndex++) {
                DataPoint point = split[0].getData().get(trainIndex);
                xInput.setInput(point.getFeatures().get(0));
                yInput.setInput(point.getFeatures().get(1));
                double label = point.getLabel();
                double weightedSum = layer2node.getWeightedSum();
                double guess = layer2node.evaluate();
                //System.out.println("Network guessed " + guess + "; answer was " + label + ": MSE is: " + mse.result(guess, label));

                weight21Adjust[trainIndex] = mse.derivative(guess, label)
                        * activationFunction.derivative(weightedSum)
                        * layer11node.evaluate()
                        *-1; // need to get NEGATIVE slope

                node21Adjust[trainIndex] = mse.derivative(guess, label)
                        * activationFunction.derivative(weightedSum)
                        * layer2node.getInputs().get(0).getWeight()
                        *-1; // need to get NEGATIVE slope

                weight22Adjust[trainIndex] = mse.derivative(guess, label)
                        * activationFunction.derivative(weightedSum)
                        * layer12node.evaluate()
                        *-1; // need to get NEGATIVE slope

                node22Adjust[trainIndex] = mse.derivative(guess, label)
                        * activationFunction.derivative(weightedSum)
                        * layer2node.getInputs().get(1).getWeight()
                        *-1; // need to get NEGATIVE slope

                bias21Adjust[trainIndex] = mse.derivative(guess, label)
                        * activationFunction.derivative(weightedSum)
                        *-1; // need to get NEGATIVE slope




                guess = layer11node.evaluate();
                label = guess + node21Adjust[trainIndex];
                weightedSum = layer11node.getWeightedSum();

                bias11Adjust[trainIndex] = mse.derivative(guess, label)
                        * activationFunction.derivative(weightedSum)
                        *-1; // need to get NEGATIVE slope

                xWeight1Adjust[trainIndex] = mse.derivative(guess, label)
                        * activationFunction.derivative(weightedSum)
                        * xInput.getInput()
                        *-1; // need to get NEGATIVE slope

                yWeight1Adjust[trainIndex] = mse.derivative(guess, label)
                        * activationFunction.derivative(weightedSum)
                        * yInput.getInput()
                        *-1; // need to get NEGATIVE slope




                guess = layer12node.evaluate();
                label = guess + node22Adjust[trainIndex];
                weightedSum = layer12node.getWeightedSum();

                bias12Adjust[trainIndex] = mse.derivative(guess, label)
                        * activationFunction.derivative(weightedSum)
                        *-1; // need to get NEGATIVE slope

                xWeight2Adjust[trainIndex] = mse.derivative(guess, label)
                        * activationFunction.derivative(weightedSum)
                        * xInput.getInput()
                        *-1; // need to get NEGATIVE slope

                yWeight2Adjust[trainIndex] = mse.derivative(guess, label)
                        * activationFunction.derivative(weightedSum)
                        * yInput.getInput()
                        *-1; // need to get NEGATIVE slope
            }
            layer2node.getInputs().get(0).incrementWeight(STEP_SIZE*mean(weight21Adjust));
            layer2node.getInputs().get(1).incrementWeight(STEP_SIZE*mean(weight22Adjust));
            layer2node.getInputs().get(2).incrementWeight(STEP_SIZE*mean(bias21Adjust));

            layer11node.getInputs().get(0).incrementWeight(STEP_SIZE*mean(xWeight1Adjust));
            layer11node.getInputs().get(1).incrementWeight(STEP_SIZE*mean(yWeight1Adjust));
            layer11node.getInputs().get(2).incrementWeight(STEP_SIZE*mean(bias11Adjust));

            layer12node.getInputs().get(0).incrementWeight(STEP_SIZE*mean(xWeight2Adjust));
            layer12node.getInputs().get(1).incrementWeight(STEP_SIZE*mean(yWeight2Adjust));
            layer12node.getInputs().get(2).incrementWeight(STEP_SIZE*mean(bias12Adjust));
        }
    }

}