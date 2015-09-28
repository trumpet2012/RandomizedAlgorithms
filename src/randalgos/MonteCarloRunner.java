package randalgos;

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
import randalgos.math.Equation;
import randalgos.math.MonteCarlo;
import randalgos.math.RandomSelect;
import randalgos.result.AlgoResult;
import randalgos.result.MonteCarloResult;
import randalgos.utils.table.EquationCell;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * William Trent Holliday
 * 9/26/15
 */
public class MonteCarloRunner extends Application {

    public static void main(String[] args){
        // Object that will be used to compute the area under the curves
        MonteCarlo monte_carlo = new MonteCarlo();

        // The list of equations to perform the monte carlo method on to get the approximate area under the curve.
        Equation[] equation_list = {
                new Equation("${\\tiny \\int_{0}^{5} x^{3}\\, dx}$", 156.25) {
                    @Override
                    public double calculate(double x) {
                        return Math.pow(x, 3);
                    }

                    @Override
                    public double x_max() {
                        return 5;
                    }

                    @Override
                    public double y_max() {
                        return 125;
                    }
                },
                new Equation("${\\tiny \\int_{1}^{10} \\ln{x}\\,dx}$", 14.026) {
                    @Override
                    public double calculate(double x) {
                        return Math.log(x);
                    }

                    @Override
                    public double x_max() {
                        return 10;
                    }

                    @Override
                    public double y_max() {
                        return 2.30259;
                    }
                },
                new Equation("${\\tiny \\int_{0}^{2} x^{2} + \\sqrt[5]{x}\\, dx}$", 4.5812) {
                    @Override
                    public double calculate(double x) {
                        return Math.pow(x, 2) + Math.pow(x, 1/5);
                    }

                    @Override
                    public double x_max() {
                        return 2;
                    }

                    @Override
                    public double y_max() {
                        return 4 + Math.pow(2, 1/5);
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

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new Group());

        int width = 800;
        int height = 650;

        primaryStage.setTitle("Monte Carlo Algorithm - Area Under Curve");

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

        TableColumn<MonteCarloResult, String> equation = new TableColumn<>("Equation");
        equation.setCellValueFactory(
                (param) ->
                    param.getValue().equation.equationName
        );
        equation.setCellFactory(
                (param) ->
                        new EquationCell()
        );

        TableColumn<MonteCarloResult, Integer> number_of_times = new TableColumn<>("# Of Guesses");
        number_of_times.setCellValueFactory(
                (param) ->
                    param.getValue().num_times.asObject()
        );
        number_of_times.setPrefWidth(120);

        TableColumn<MonteCarloResult, String> approx_result = new TableColumn<>("Approx. Area");
        approx_result.setCellValueFactory(
                (param) ->
                    param.getValue().approx_result
        );
        approx_result.setPrefWidth(120);

        TableColumn<MonteCarloResult, Double> actual_result = new TableColumn<>("Exact Area");
        actual_result.setCellValueFactory(
                (param) ->
                    param.getValue().equation.actual_area.asObject()
        );
        actual_result.setPrefWidth(120);

        TableColumn<MonteCarloResult, String> error_percent = new TableColumn<>("Error %");
        error_percent.setCellValueFactory(
                (param) ->
                    param.getValue().error_percentage
        );
        error_percent.setPrefWidth(120);

        table.setItems(table_data);
        table.getColumns().addAll(equation, number_of_times, approx_result, actual_result, error_percent);

        String css_file = this.getClass().getResource("styles/javafx-tables.css").toExternalForm();
        table.getStylesheets().add(css_file);

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
