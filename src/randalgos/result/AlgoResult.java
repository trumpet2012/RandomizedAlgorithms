package randalgos.result;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;

/**
 * William Trent Holliday
 * 9/26/15
 */
public class AlgoResult {
    public SimpleIntegerProperty number_of_items;
    public SimpleLongProperty exec_time;
    public SimpleIntegerProperty result;

    public AlgoResult(int number_of_items, long exec_time, int result) {
        this.number_of_items = new SimpleIntegerProperty(number_of_items);
        this.exec_time = new SimpleLongProperty(exec_time);
        this.result = new SimpleIntegerProperty(result);
    }
}
