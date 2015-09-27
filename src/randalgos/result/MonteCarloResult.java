package randalgos.result;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import randalgos.math.Equation;

/**
 * William Trent Holliday
 * 9/26/15
 *
 * Object for storing the results of an execution of the monte carlo method which determines the area under a curve.
 */
public class MonteCarloResult {

    public Equation equation;
    public SimpleIntegerProperty num_times;    // We use Simple properties here so that it is easy to load the information
    public SimpleDoubleProperty approx_result; // into a JavaFX table.

    /**
     * Create a new instance which will hold the results of running the given equation the specified number of times.
     * @param equation - The equation that area was calculated for.
     * @param num_times - The number of random points that were used to approximate the area under the curve.
     * @param approx_result - The approximate area that was found by using the monte carlo method.
     */
    public MonteCarloResult(Equation equation, int num_times, double approx_result){
        this.equation = equation;
        this.num_times = new SimpleIntegerProperty(num_times);
        this.approx_result = new SimpleDoubleProperty(approx_result);
    }

}
