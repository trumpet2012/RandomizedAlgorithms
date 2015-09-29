package randalgos;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import randalgos.math.Equation;
import randalgos.math.MonteCarlo;
import randalgos.result.MonteCarloResult;
import randalgos.utils.EquationImageBuilder;
import randalgos.utils.table.EquationCell;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * William Trent Holliday
 * 9/26/15
 */
public class MonteCarloRunner {

    private static void exec(){
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

    }

    public static void show(){
        exec();

        Stage monteCarloStage = new Stage();
        Scene scene = new Scene(new Group());

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        int width = 800;
        int height = 650;

        monteCarloStage.setTitle("Monte Carlo Algorithm - Area Under Curve");

        monteCarloStage.setWidth(width);
        monteCarloStage.setHeight(height);

        table.setPrefWidth(width);
        table.setPrefHeight(height);

        monteCarloStage.widthProperty().addListener(
                (observable, oldValue, newValue) ->
                        tabPane.setPrefWidth(newValue.doubleValue())
        );

        monteCarloStage.heightProperty().addListener(
                (observable, oldValue, newValue) ->
                        tabPane.setPrefHeight(newValue.doubleValue() - 27)
        );

        BorderPane graphPanel = new BorderPane();
        FlowPane graphKey = new FlowPane();

        graphKey.setAlignment(Pos.CENTER);

        populate_table();
        populate_graph(graphKey);

        graphPanel.setCenter(graph);
        graphPanel.setBottom(graphKey);

        Tab tableTab = new Tab("Table data");
        tableTab.setContent(table);

        Tab graphTab = new Tab("Graph");
        graphTab.setContent(graphPanel);

        tabPane.getTabs().addAll(tableTab, graphTab);

        ((Group) scene.getRoot()).getChildren().addAll(tabPane);

        monteCarloStage.setScene(scene);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        monteCarloStage.setX((screenBounds.getWidth() - monteCarloStage.getWidth()) / 2);
        monteCarloStage.setX((screenBounds.getHeight() - monteCarloStage.getHeight()) / 4);
        monteCarloStage.show();
    }

    private static void populate_table(){
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
                (param) -> {
                    SimpleStringProperty property = new SimpleStringProperty(String.format("%.4f", param.getValue().error_percentage.getValue()));
                    return property;
                }
        );
        error_percent.setPrefWidth(120);

        table.setItems(table_data);
        table.getColumns().addAll(equation, number_of_times, approx_result, actual_result, error_percent);

        String css_file = MonteCarloRunner.class.getResource("styles/javafx-tables.css").toExternalForm();
        table.getStylesheets().add(css_file);
    }

    private static void populate_graph(FlowPane graphKey){
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setAutoRanging(true);
        xAxis.setForceZeroInRange(false);
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Guesses");
        yAxis.setLabel("Error Percentage (%)");

        final LineChart<Number, Number> errorPercentageChart = new LineChart<Number, Number>(xAxis, yAxis);
        errorPercentageChart.setTitle("Error Percentage Per Equation");
        int equation_count = 1;
        HashMap<Equation, ArrayList<HashMap<Integer, MonteCarloResult>>> equation_groups = create_equation_groups(table_data);
        for(Equation equation : equation_groups.keySet()) {
            ArrayList<HashMap<Integer, MonteCarloResult>> equation_dict_list = equation_groups.get(equation);
            XYChart.Series series = new XYChart.Series();

            graphKey.getChildren().add(createEquationKey(series, equation, equation_count));
            for(HashMap<Integer, MonteCarloResult> result : equation_dict_list){
                for(Integer num_times: result.keySet()){
                    series.getData().add(new XYChart.Data<>(num_times, result.get(num_times).error_percentage.getValue()));
                }
            }
            errorPercentageChart.getData().add(series);
            equation_count++;
        }

        graph = errorPercentageChart;
    }

    private static Node createEquationKey(XYChart.Series series, Equation equation, int equation_count){
        ImageView equation_image = new ImageView(EquationImageBuilder.create_image(equation.equationName.getValue()));
        String series_name = "Equation " + equation_count;
        series.setName(series_name);

        Label equation_key = new Label(series_name);
        equation_key.setPadding(new Insets(0, 0, 0, 15));

        BorderPane equation_tile = new BorderPane();
        equation_tile.setCenter(equation_image);
        equation_tile.setBottom(equation_key);
        equation_tile.setPadding(new Insets(10));

        return equation_tile;
    }

    private static HashMap<Equation, ArrayList<HashMap<Integer, MonteCarloResult>>> create_equation_groups(ObservableList<MonteCarloResult> table_data){
        ArrayList<Equation> equations = get_equations(table_data);

        HashMap<Equation, ArrayList<HashMap<Integer, MonteCarloResult>>> equation_dict = new HashMap<>();
        for(int index = 0; index< equations.size(); index++){
            Equation equation = equations.get(index);

            ArrayList<HashMap<Integer, MonteCarloResult>> equation_dict_list = new ArrayList<>();
            for (int jPos = 0; jPos < table_data.size(); jPos++){

                MonteCarloResult result = table_data.get(jPos);
                HashMap<Integer, MonteCarloResult> result_dict = new HashMap<>();
                if (result.equation == equation) {
                    result_dict.put(result.num_times.getValue(), result);

                    if (!is_in(equation_dict_list, result_dict))
                        equation_dict_list.add(result_dict);
                }
            }
            equation_dict.put(equation, equation_dict_list);
        }

        return equation_dict;
    }

    private static boolean is_in(ArrayList<HashMap<Integer, MonteCarloResult>> equation_dict_list, HashMap<Integer, MonteCarloResult> result_dict){
        for(HashMap<Integer, MonteCarloResult> map: equation_dict_list){
            if(map.containsKey(result_dict.keySet().toArray()[0])){
                return true;
            }
        }
        return false;
    }

    private static ArrayList<Equation> get_equations(ObservableList<MonteCarloResult> table_data){
        ArrayList<Equation> equation_list = new ArrayList<>();
        for (MonteCarloResult result : table_data){
            if(!equation_list.contains(result.equation)){
                equation_list.add(result.equation);
            }
        }
        return equation_list;
    }

    private static final int[] number_of_elements = {1000, 10000, 100000, 1000000};
    private static final TableView table = new TableView();
    private static LineChart graph;
    private static ObservableList<MonteCarloResult> table_data = FXCollections.observableArrayList();
}
