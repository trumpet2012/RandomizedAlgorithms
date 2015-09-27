import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;


/**
 * William Trent Holliday
 * 9/26/15
 */
abstract class Equation{
    public SimpleStringProperty equationName;
    public SimpleDoubleProperty actual_area;

    public Equation(String name, double actual_area){
        this.equationName = new SimpleStringProperty(name);
        this.actual_area = new SimpleDoubleProperty(actual_area);
    }

    abstract double calculate(double x);
    abstract double x_max();
    abstract double y_max();
}
