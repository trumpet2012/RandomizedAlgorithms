package randalgos.result;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import randalgos.math.Equation;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * William Trent Holliday
 * 9/26/15
 *
 * Object for storing the results of an execution of the monte carlo method which determines the area under a curve.
 */
public class MonteCarloResult {

    public Equation equation;
    public SimpleIntegerProperty num_times;    // We use Simple properties here so that it is easy to load the information
    public SimpleStringProperty approx_result; // into a JavaFX table.
    public SimpleStringProperty error_percentage;

    /**
     * Create a new instance which will hold the results of running the given equation the specified number of times.
     * @param equation - The equation that area was calculated for.
     * @param num_times - The number of random points that were used to approximate the area under the curve.
     * @param approx_result - The approximate area that was found by using the monte carlo method.
     */
    public MonteCarloResult(Equation equation, int num_times, double approx_result){
        this.equation = equation;
        this.num_times = new SimpleIntegerProperty(num_times);
        NumberFormat formatter = new DecimalFormat("#0.0000");
        this.approx_result = new SimpleStringProperty(formatter.format(approx_result));
        double error_percent = calculate_error(equation.actual_area.doubleValue(), approx_result);
        this.error_percentage = new SimpleStringProperty(String.format("%.3f", error_percent) + "%");
    }

    private double calculate_error(double actual_area, double approx_result){
        return ((Math.abs(actual_area - approx_result)) / Math.abs(actual_area)) * 100;
    }

}
