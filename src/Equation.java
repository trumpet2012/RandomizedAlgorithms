import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;


/**
 * William Trent Holliday
 * 9/26/15
 *
 * Abstract class used to create new equations which can be stored and used to run the monte carlo method against. All
 * implementations of this class must define values for the calculate, x_max, and y_max methods.
 *
 * This equation wrapper only allows for equations that have one variable.
 */
abstract class Equation{
    public SimpleStringProperty equationName;
    public SimpleDoubleProperty actual_area;

    public Equation(String name, double actual_area){
        this.equationName = new SimpleStringProperty(name);
        this.actual_area = new SimpleDoubleProperty(actual_area);
    }

    /**
     * Method that will be used to calculate the value that the equation returns based on the given variable.
     * @param x - the x value to be used to calculate the value for.
     *          Ex.
     *              To represent the equation f(x) = x^2 you would define the calculate function to return:
     *              Math.pow(x, 2)
     * @return the result of computing the function at the specified x location.
     */
    abstract double calculate(double x);

    /**
     * The furthest x position that will be used to calculate the area.
     * @return a double
     */
    abstract double x_max();

    /**
     * The biggest y position that the function contains in the range of x positions.
     * @return a double
     */
    abstract double y_max();
}
