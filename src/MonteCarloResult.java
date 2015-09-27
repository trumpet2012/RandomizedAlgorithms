import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * William Trent Holliday
 * 9/26/15
 */
public class MonteCarloResult {

    Equation equation;
    SimpleIntegerProperty num_times;
    SimpleDoubleProperty approx_result;

    public MonteCarloResult(Equation equation, int num_times, double approx_result){
        this.equation = equation;
        this.num_times = new SimpleIntegerProperty(num_times);
        this.approx_result = new SimpleDoubleProperty(approx_result);
    }

}
