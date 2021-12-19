package SimplePerceptron;

import online.umbcraft.data.inputs.DataPoint;
import online.umbcraft.data.inputs.DataSet;
import online.umbcraft.data.jframe.DataGrapher;
import online.umbcraft.data.offsets.OffsetVector;
import online.umbcraft.ml.activations.TanhFunction;
import online.umbcraft.ml.component.Connection;
import online.umbcraft.ml.component.Layer;
import online.umbcraft.ml.component.nodes.TLUNode;
import online.umbcraft.ml.costs.MeanSquaredError;
import online.umbcraft.ml.perceptron.Perceptron;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MultiLayerPercept {


    final static public Random RANDOM = new Random(420);


    public static void main(String[] args) throws InterruptedException {
        Perceptron perceptron = new Perceptron(
                new TanhFunction(),
                new MeanSquaredError(),
                2, 8, 8, 8, 8, 1
        );

        DataSet dataset = new DataSet();
        DataGrapher graph = new DataGrapher(dataset);

        // dataset of size 10k
        for (int i = 0; i < 10000; i++) {
            double x = (RANDOM.nextDouble() - 0.5) * 2;
            double y = (RANDOM.nextDouble() - 0.5) * 2;

            List<Double> features = new ArrayList<>();
            features.add(x);
            features.add(y);

            // function to imitate
            boolean fx = (x*x +y*y < 0.5*0.5);


            double label = (fx) ? 1 : -1;
            DataPoint newPoint = new DataPoint(
                    new double[]{x, y},
                    new double[]{label});
            dataset.add(newPoint);
            graph.addPoint_g(x,y,(fx) ? Color.BLUE: Color.RED);
        }
        DataSet[] split = dataset.splitSet(0.25);
        DataSet trainingData = split[0];
        DataSet testingData = split[1];
        DataSet[] batches = trainingData.batches(15);





        // 10k epochs!
        int epoch = 0;
        for(int i = 0; i < 10000; i++) {
            double totalError = 0;
            double testSize = testingData.getData().size();

            for(DataPoint testPoint : testingData.getData()) {
                perceptron.setFeatures(testPoint);
                double guess = perceptron.evaluate(testPoint.getFeatures()[0], testPoint.getFeatures()[1])[0];
                graph.addPoint_m(
                        testPoint.getFeatures()[0],
                        testPoint.getFeatures()[1],
                        (guess < 0)?Color.RED: Color.BLUE
                );
                double cost = perceptron.getErrorFunction().result(guess, testPoint.getLabels()[0]);
                totalError += cost;
            }
            double avgError = totalError / testSize;
            System.out.println("----Epoch "+epoch+": error is "+avgError);


            // train

            for(DataSet batch : batches)
                perceptron.trainIteration(batch, 0.1);

//            Layer output = perceptron.getLayers().get(1);
//            TLUNode node = (TLUNode) output.getNodes().get(0);
//            List<Connection> conns = node.getTLU().getInputs();
//
//            OffsetVector total = null;
//            for(DataPoint dp: trainingData.getData()) {
//                //System.out.println("datapoint: \n"+dp);
//                perceptron.setFeatures(dp);
//                OffsetVector ov = output.train(dp.getLabels()).get(0);
//                if(total == null)
//                    total = ov;
//                else {
//                    total = total.add(ov);
//                }
//            }
//            //System.out.println("offsetting by \n"+total);
//            conns.get(0).incrementWeight(0.1 * (total.weightOffsets()[0] / trainingData.getData().size()));
//            conns.get(1).incrementWeight(0.1 * (total.weightOffsets()[1] / trainingData.getData().size()));
//            conns.get(2).incrementWeight(0.1 * (total.biasOffset()/ trainingData.getData().size()));

            epoch++;
        }
    }
}
