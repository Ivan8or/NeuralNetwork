package online.umbcraft.data;

import online.umbcraft.NeuralNetwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


// holds a list of datapoints
public class DataSet {

    final private List<DataPoint> data;

    public DataSet() {
        data = new ArrayList<>();
    }

    // adds a new datapoint to the set
    public void add(DataPoint datapoint) {
        data.add(datapoint);
    }

    public List<DataPoint> getData() {
        return data;
    }

    // returns the dimensions of the dataset:
    // index 0 is the row (feature) size, index 1 is the column (dataset) size
    public int[] dimensions() {
        int dataDim = -1;
        if(data.size() > 0)
            dataDim = data.get(0).dimension();

        return new int[]{dataDim, data.size()};
    }

    // mixes the data randomly
    public void shuffle() {
        Collections.shuffle(data, NeuralNetwork.RANDOM);
    }


    public DataSet[] batches(int numBatches) {
        DataSet[] batches = new DataSet[numBatches];
        for(int i = 0; i < numBatches; i++) {
            batches[i] = new DataSet();
        }
        for(int i = 0; i < data.size(); i++) {
            batches[i % numBatches].add(data.get(i));
        }
        return batches;
    }

    // returns two datasets: training and testing
    public DataSet[] splitSet(double trainRatio) {
        DataSet training = new DataSet();
        DataSet testing = new DataSet();

        // split data approximately correctly
        for(DataPoint dp : data) {
            if(NeuralNetwork.RANDOM.nextDouble() < trainRatio)
                training.add(dp);
            else
                testing.add(dp);
        }

        // if training ended up too big, move excess over
        while(training.data.size() / (double)(training.data.size() + testing.data.size()) >= trainRatio) {
            DataPoint toMove = training.data.get(0);
            training.data.remove(0);
            testing.add(toMove);
        }

        // if testing ended up too big, move excess over
        while(testing.data.size() / (double)(training.data.size() + testing.data.size()) >= (1-trainRatio)) {
            DataPoint toMove = testing.data.get(0);
            testing.data.remove(0);
            training.add(toMove);
        }

        return new DataSet[]{training, testing};
    }
}
