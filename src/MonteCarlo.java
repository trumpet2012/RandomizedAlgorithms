import java.util.concurrent.Callable;

/**
 * William Trent Holliday
 * 9/26/15
 */
public class MonteCarlo {

    class Point{
        double x, y;
        public Point(double x, double y){
            this.x = x;
            this.y = y;
        }
    }

    private Point find_point(double x_max, double y_max){
        return new Point(Math.random() * x_max, Math.random() * y_max);
    }

    public boolean check_point(Equation equation, Point point){
        Double equation_y = equation.calculate(point.x);
        return point.y <= equation_y;
    }

    public double calculate_area(Equation equation, int num_points){
        int number_of_success = 0;
        for (int i=0; i<num_points; i++){
            if (check_point(equation, find_point(equation.x_max(), equation.y_max()))){
                number_of_success++;
            }
        }
        return ((double) number_of_success/ (double) num_points) * (equation.y_max() * equation.x_max());
    }

}
