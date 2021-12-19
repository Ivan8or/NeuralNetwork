package online.umbcraft.ml.perceptron;

import online.umbcraft.data.inputs.DataPoint;
import online.umbcraft.data.inputs.DataSet;
import online.umbcraft.data.offsets.OffsetVector;
import online.umbcraft.ml.activations.ActivationFunction;
import online.umbcraft.ml.component.Connection;
import online.umbcraft.ml.component.Layer;
import online.umbcraft.ml.component.ThresholdLogicUnit;
import online.umbcraft.ml.component.nodes.Node;
import online.umbcraft.ml.component.nodes.PassthroughNode;
import online.umbcraft.ml.component.nodes.TLUNode;
import online.umbcraft.ml.costs.ErrorFunction;

import java.util.ArrayList;
import java.util.List;

public class Perceptron {

    final private List<Layer> layers;
    final private ErrorFunction ef;
    final private ActivationFunction af;

    // creates the network of nodes that dictate the perceptron's decision
    public Perceptron(ActivationFunction af, ErrorFunction ef, int... nodesPerLayer) {
        if(nodesPerLayer.length < 2)
            throw new IllegalArgumentException("Perceptron must have at least 2 layers!");

        this.layers = new ArrayList<>(nodesPerLayer.length);
        this.ef = ef;
        this.af = af;

        // create input layer with exclusively PassthroughNodes (and a hidden bias)
        Layer inputLayer = new Layer(this);
        for(int inputCount = 0; inputCount < nodesPerLayer[0]; inputCount++) {
            inputLayer.add(new PassthroughNode());
        }

        // create hidden layers + output layer out of TLU Nodes
        for(int layer = 1; layer < nodesPerLayer.length; layer++) {
            int nodeCount = nodesPerLayer[layer];

            // fill in the correct amount of nodes each layer
            for(int node = 0; node < nodeCount; node++) {

                // create connections to each node in previous layer
                List<Connection> connections = new ArrayList<>();
                for(Node c: layers.get(layer-1).getNodes())
                    connections.add(new Connection(c));

                // build new node
                ThresholdLogicUnit nextTLU = new ThresholdLogicUnit(connections, af);
                Node nextNode = new TLUNode(nextTLU);
                layers.get(layer).add(nextNode);
            }
        }
    }

    // uses values from datapoint to fill features
    public void setFeatures(DataPoint inputs) {
        List<Node> inputNodes = layers.get(0).getNodes();
        double[] features = inputs.getFeatures();

        // ensure # of features is correct
        if(inputNodes.size() != features.length)
            throw new IllegalArgumentException("Invalid number of input parameters! expected "
                    +layers.get(0).getNodes().size());

        // fill in all inputs
        for(int i = 0; i < inputNodes.size(); i++) {
            ((PassthroughNode)inputNodes.get(i)).setInput(features[i]);
        }
    }

    // uses values from array to fill features
    public void setFeatures(double... inputs) {
        List<Node> inputNodes = layers.get(0).getNodes();

        // ensure # of features is correct
        if(inputNodes.size() != inputs.length)
            throw new IllegalArgumentException("Invalid number of input parameters! expected "
                    +layers.get(0).getNodes().size());

        // fill in all inputs
        for(int i = 0; i < inputNodes.size(); i++) {
            ((PassthroughNode)inputNodes.get(i)).setInput(inputs[i]);
        }
    }

    // evaluates the current features - returns a list of probabilities
    public double[] evaluate(double... inputs) {

        // accumulate all node evaluation outputs
        List<Node> outputNodes = layers.get(layers.size()-1).getNodes();
        int numOutputs = outputNodes.size();
        double[] results = new double[numOutputs];
        for(int i = 0; i < numOutputs; i++) {
            results[i] = outputNodes.get(i).getValue();
        }
        return results;
    }

    // performs a gradient descent step towards the lowest cost this dataset produces
    public void train(DataSet dataset, double stepSize) {

        for(DataPoint dp: dataset.getData()) {
            setFeatures(dp);
            double[] goals = dp.getLabels();

            List<List<OffsetVector>> allLayersOffsets = new ArrayList<>();

            // calculates offsets for each node in each layer of the perceptron
            for(int layerIndex = layers.size()-1; layerIndex > 0; layerIndex--) {
                Layer layer = layers.get(layerIndex);
                List<OffsetVector> layerOffsets = layer.train(goals);
                allLayersOffsets.add(layerOffsets);

                // sets goals for next layer back as
                // their current val + the average of the node offsets for this layer
                goals = new double[layerOffsets.get(0).nodeOffsets().length];
                for(int vectorIndex = 0; vectorIndex < layerOffsets.size(); vectorIndex++) {
                    OffsetVector ov = layerOffsets.get(vectorIndex);

                    ov.nodeOffsets();
                    //for()
                }
            }
        }
    }

    public ErrorFunction getErrorFunction() {
        return ef;
    }
    public ActivationFunction getActivationFunction() {
        return af;
    }
}
