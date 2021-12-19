package online.umbcraft.ml.component;

import online.umbcraft.NeuralNetwork;
import online.umbcraft.ml.component.nodes.Node;

public class Connection {

    final private Node node;
    private double weight;

    public Connection(Node node) {
        this.node = node;
        weight = (NeuralNetwork.RANDOM.nextDouble() - 0.5);
    }

    public Connection(Node node, double weight) {
        this.node = node;
        this.weight = weight;
    }

    public Node getInput() {
        return node;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double newWeight) {
        this.weight = newWeight;
    }

    public void incrementWeight(double increment) {
        this.weight += increment;
    }

    public double getConnectionValue() {
        return weight * node.getValue();
    }

    public double getRawValue() {
        return node.getValue();
    }
}
