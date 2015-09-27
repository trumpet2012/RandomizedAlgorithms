
import javafx.scene.control.TableCell;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * William Trent Holliday
 * 9/26/15
 */
public class AlgoRunner extends Application {

    public static void main(String[] args){
        RandomSelect random_select = new RandomSelect();

        AlgoResult[] results = new AlgoResult[number_of_elements.length];
        int position = 3; // Third smallest

        for (int i = 0; i < results.length; i++){
            int[] arr = rand_array(number_of_elements[i]);
            results[i] = random_select.execute(arr, position);
        }

        // Object that will be used to compute the area under the curves
        MonteCarlo monte_carlo = new MonteCarlo();

        // The list of equations to perform the monte carlo method on to get the approximate area under the curve.
        Equation[] equation_list = {
                new Equation("${\\tiny \\int_{0}^{5} x^{2}\\, dx}$", 41.667) {
                    @Override
                    double calculate(double x) {
                        return Math.pow(x, 2);
                    }

                    @Override
                    double x_max() {
                        return 5;
                    }

                    @Override
                    double y_max() {
                        return 25;
                    }
                },
                new Equation("${\\tiny \\int_{0}^{5} x^{3}\\, dx}$", 156.25) {
                    @Override
                    double calculate(double x) {
                        return Math.pow(x, 3);
                    }

                    @Override
                    double x_max() {
                        return 5;
                    }

                    @Override
                    double y_max() {
                        return 125;
                    }
                },
                new Equation("${\\tiny \\int_{1}^{10} \\ln{x}\\,dx}$", 14.026) {
                    @Override
                    double calculate(double x) {
                        return Math.log(x);
                    }

                    @Override
                    double x_max() {
                        return 10;
                    }

                    @Override
                    double y_max() {
                        return 2.30259;
                    }
                }
        };

        ArrayList<MonteCarloResult> equation_results = new ArrayList<>();

        for (Equation equation : equation_list){
            for(int num_times : number_of_elements) {
                Double equation_answer = monte_carlo.calculate_area(equation, num_times);
                equation_results.add(new MonteCarloResult(equation, num_times, equation_answer));
            }
        }

        table_data.addAll(equation_results);

        launch(args);
    }

    private static int[] rand_array(int size){
        int min_rand = 0;
        int max_rand = 100000;

        int[] arr = new int[size];
        for (int i = 0; i < size; i++){
            arr[i] = ThreadLocalRandom.current().nextInt(min_rand, max_rand);
        }

        return arr;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new Group());

        int width = 800;
        int height = 650;

        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
        table.setPrefWidth(width);
        table.setPrefHeight(height);

        primaryStage.widthProperty().addListener(
                (observable, oldValue, newValue) ->
                        table.setPrefWidth(newValue.doubleValue())
        );

        primaryStage.heightProperty().addListener(
                (observable, oldValue, newValue) ->
                        table.setPrefHeight(newValue.doubleValue() - 27)
        );

        TableColumn<MonteCarloResult, String> equation = new TableColumn<MonteCarloResult, String>("Equation");
        equation.setCellValueFactory(
                (param) ->
                    param.getValue().equation.equationName
        );
        equation.setCellFactory(
                (param) -> {
                    TableCell<MonteCarloResult, String> cell = new MonteCarloCell();
                    return cell;
                }
        );

        ArrayList<TableColumn<MonteCarloResult, String>> execution_number_list = new ArrayList<>();

        TableColumn<MonteCarloResult, Integer> number_of_times = new TableColumn<MonteCarloResult, Integer>("# Of Guesses");
        number_of_times.setCellValueFactory(
                (param) ->
                    param.getValue().num_times.asObject()
        );

        TableColumn<MonteCarloResult, Double> approx_result = new TableColumn<MonteCarloResult, Double>("Approx. Area");
        approx_result.setCellValueFactory(
                (param) ->
                    param.getValue().approx_result.asObject()
            );

        TableColumn<MonteCarloResult, Double> actual_result = new TableColumn<MonteCarloResult, Double>("Exact Area");
        actual_result.setCellValueFactory(
                (param) ->
                    param.getValue().equation.actual_area.asObject()
        );

        table.setItems(table_data);
        table.getColumns().addAll(equation, number_of_times, approx_result, actual_result);

        final VBox vbox = new VBox();
        vbox.setSpacing(100);
        vbox.setPadding(new Insets(0, 0, 0, 0));
        vbox.getChildren().addAll(table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private static final int[] number_of_elements = {1000, 10000, 100000, 1000000};
    private final TableView table = new TableView();
    private static ObservableList<MonteCarloResult> table_data = FXCollections.observableArrayList();
}
