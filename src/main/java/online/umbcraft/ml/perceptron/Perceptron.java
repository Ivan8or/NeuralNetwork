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

import java.awt.*;
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
        layers.add(inputLayer);

        // create hidden layers + output layer out of TLU Nodes
        for(int layer = 1; layer < nodesPerLayer.length; layer++) {
            int nodeCount = nodesPerLayer[layer];

            // fill in the correct amount of nodes each layer
            Layer nextLayer = new Layer(this);
            for(int node = 0; node < nodeCount; node++) {

                // create connections to each node in previous layer
                List<Connection> connections = new ArrayList<>();
                for(Node c: layers.get(layer-1).getNodes())
                    connections.add(new Connection(c));
                // create connection to bias
                connections.add(new Connection(layers.get(layer-1).getBias()));

                // build new node
                ThresholdLogicUnit nextTLU = new ThresholdLogicUnit(connections, af);
                Node nextNode = new TLUNode(nextTLU);
                nextLayer.add(nextNode);
            }
            layers.add(nextLayer);
        }
    }

    public double performTest(DataSet testSet) {

        double totalError = 0;
        for(DataPoint testPoint : testSet.getData()) {
            setFeatures(testPoint);
            double[] guesses = evaluate(testPoint.getFeatures());

            for(int labelIndex = 0; labelIndex < testPoint.getLabels().length; labelIndex++) {
                double label = testPoint.getLabels()[labelIndex];
                double guess = guesses[labelIndex];
                totalError += ef.result(guess, label);
            }
        }
        double avgError = totalError / (testSet.getData().size() * testSet.getData().get(0).getLabels().length);
        return avgError;
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
    public void trainIteration(DataSet dataset, double stepSize) {

        List<List<OffsetVector>> sumAllLayers = null;

        for(DataPoint dp: dataset.getData()) {
            setFeatures(dp);
            double[] goals = dp.getLabels();

            // list of layers, of nodes in the layer, of all desired offsets for a node (for one piece of data)
            // in order from left to right; index 0 is first real layer (not passthrough)
            List<List<OffsetVector>> allLayers = new ArrayList<>();

            // calculates offsets for each node in each layer of the perceptron
            // goes backwards to allow for previous layers to access the offsets they need to know
            for(int layerIndex = layers.size()-1; layerIndex > 0; layerIndex--) {
                Layer layer = layers.get(layerIndex);
                List<OffsetVector> layerOffsets = layer.train(goals);
                allLayers.add(0,layerOffsets);

                // sets goals for next layer back as
                // their current val + the average of the node offsets for this layer
                goals = new double[layerOffsets.get(0).nodeOffsets().length];

                for(int vectorIndex = 0; vectorIndex < layerOffsets.size(); vectorIndex++) {
                    OffsetVector ov = layerOffsets.get(vectorIndex);

                    // sum up all desired offsets for each previous node by the current nodes
                    for(int offIndex = 0; offIndex < ov.nodeOffsets().length; offIndex++) {
                        double offset = ov.nodeOffsets()[offIndex];
                        goals[offIndex] += offset;
                    }
                }

                // add original prev. node values to the desired offsets to create a new goal for them
                for(int guessIndex = 0; guessIndex < layers.get(layerIndex-1).getNodes().size(); guessIndex++) {
                    goals[guessIndex] += layers.get(layerIndex-1).getNodes().get(guessIndex).getValue();
                }
            }

            if(sumAllLayers == null)
                sumAllLayers = allLayers;
            else {
                // for each layer in the perceptron
                for (int i = 0; i < allLayers.size(); i++) {
                    List<OffsetVector> nextLayer = allLayers.get(i);
                    List<OffsetVector> sumNextLayer = sumAllLayers.get(i);

                    // for each node in the layer
                    for (int j = 0; j < nextLayer.size(); j++) {
                        OffsetVector nextNode = nextLayer.get(j);
                        OffsetVector sumNextNode = sumNextLayer.get(j);

                        sumNextLayer.set(j, sumNextNode.add(nextNode));
                    }
                }
            }
        }

        int setSize = dataset.getData().size();

        // update weights accordingly:
        // for each layer
        for(int layerIndex = 0; layerIndex < sumAllLayers.size(); layerIndex++) {
            Layer layer = layers.get(layerIndex+1);
            List<OffsetVector> nextLayer = sumAllLayers.get(layerIndex);

            // for each node in the layer
            for(int nodeIndex = 0; nodeIndex < nextLayer.size(); nodeIndex++) {
                TLUNode node = (TLUNode) layer.getNodes().get(nodeIndex);
                List<Connection> nodeInputs = node.getTLU().getInputs();
                OffsetVector nextNode = nextLayer.get(nodeIndex);

                // for each previous node's connection to the node
                for(int connIndex = 0; connIndex < nodeInputs.size()-1; connIndex++) {
                    Connection input = nodeInputs.get(connIndex);

                    // offset the weight by the average of the desired offsets * the step size
                    input.incrementWeight(stepSize * (nextNode.weightOffsets()[connIndex] / setSize));
                }

                // for the bias
                Connection input = nodeInputs.get(nodeInputs.size()-1);
                input.incrementWeight(stepSize * (nextNode.biasOffset() / setSize));
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
