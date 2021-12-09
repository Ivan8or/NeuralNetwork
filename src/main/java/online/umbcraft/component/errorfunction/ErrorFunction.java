package online.umbcraft.component.errorfunction;

import online.umbcraft.data.DataSet;

public interface ErrorFunction {

    double error(DataSet predictions);

}
