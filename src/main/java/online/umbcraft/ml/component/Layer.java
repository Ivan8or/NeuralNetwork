package online.umbcraft.ml.component;

import online.umbcraft.data.offsets.OffsetVector;
import online.umbcraft.ml.functions.activations.ActivationFunction;
import online.umbcraft.ml.component.nodes.BiasNode;
import online.umbcraft.ml.component.nodes.Node;
import online.umbcraft.ml.component.nodes.TLUNode;
import online.umbcraft.ml.functions.costs.ErrorFunction;
import online.umbcraft.ml.perceptron.Perceptron;

import java.util.ArrayList;
import java.util.List;

public class Layer {

    final private List<Node> nodes;
    final private Node bias;

    final private Perceptron perceptron;

    public Layer(Perceptron perceptron) {
        this.perceptron = perceptron;
        nodes = new ArrayList<>();
        bias = new BiasNode();
    }
    public Layer(Perceptron perceptron, int size) {
        this.perceptron = perceptron;
        nodes = new ArrayList<>(size);
        bias = new BiasNode();
    }

    public void add(Node node) {
        nodes.add(node);
    }
    public List<Node> getNodes() {
        return nodes;
    }
    public Node getBias() {
        return bias;
    }
    public int size() {
        return nodes.size();
    }

    // returns an array containing all of the weights of each node in the layer
    public double[][] getLayerWeights() {
        double[][] toReturn = new double[nodes.size()][];
        for(int i = 0; i < nodes.size(); i++) {
            TLUNode node = (TLUNode) nodes.get(i);
            toReturn[i] = node.getTLU().getWeights();
        }
        return toReturn;
    }

    // set all weights in this layer from an array
    public void setLayerWeights(double[][] weights) {
        for(int i = 0; i < nodes.size(); i++) {
            TLUNode node = (TLUNode) nodes.get(i);
            node.getTLU().setWeights(weights[i]);
        }
    }

    public List<OffsetVector> train(final double[] labels) {

        List<OffsetVector> allOffsets = new ArrayList<>();

        ActivationFunction af = perceptron.getActivationFunction();
        ErrorFunction ef = perceptron.getErrorFunction();

        // get all offsets for each node
        for(int nodeIndex = 0; nodeIndex < nodes.size(); nodeIndex++) {
            Node n = nodes.get(nodeIndex);
            ThresholdLogicUnit tlu = ((TLUNode)n).getTLU();
            List<Connection> nodeInputs = tlu.getInputs();
            double weightedSum = tlu.getWeightedSum();
            double guess = tlu.evaluate();

            OffsetVector offsets = new OffsetVector();
            double[] weightOffsets = new double[nodeInputs.size()-1];
            double[] nodeOffsets = new double[nodeInputs.size()-1];

            // get node bias offset
            double biasOffset = ef.derivative(guess, labels[nodeIndex])
                    * af.derivative(weightedSum)
                    * -1; // need to get NEGATIVE slope

            // get weight and node offsets for each non-bias connection
            for(int connIndex = 0; connIndex < nodeInputs.size()-1; connIndex++) {
                Connection conn = nodeInputs.get(connIndex);

                weightOffsets[connIndex] = biasOffset
                        * conn.getInput().getValue();
                nodeOffsets[connIndex] = biasOffset
                        * conn.getWeight();
            }

            offsets.setBiasOffset(biasOffset);
            offsets.setWeightOffsets(weightOffsets);
            offsets.setNodeOffsets(nodeOffsets);
            allOffsets.add(offsets);
        }

        return allOffsets;
    }
}
