package randalgos;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
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

import java.util.*;

/**
 * William Trent Holliday
 * 9/26/15
 */
public class MonteCarloRunner {

    /**
     * Computes the results of running the Monte Carlo algorithm to approximate the area under the curve for the
     * equations.
     * @return an ArrayList containing the results of the algorithm.
     */
    private static ArrayList<MonteCarloResult> exec(){
        // Object that will be used to compute the area under the curves
        MonteCarlo monte_carlo = new MonteCarlo();

        // The list of equations to perform the monte carlo method on to get the approximate area under the curve.
        Equation[] equation_list = {
                new Equation("${\\tiny \\int_{0}^{5} x^{3}\\, dx}$", (625.0/4.0)) {
                    @Override
                    public double calculate(double x) {
                        return Math.pow(x, 3.0);
                    }

                    @Override
                    public double x_max() {
                        return 5.0;
                    }

                    @Override
                    public double y_max() {
                        return 125.0;
                    }
                },
                new Equation("${\\tiny \\int_{1}^{10} \\ln{x}\\,dx}$", ((10.0 * Math.log(10.0)) - 9.0)) {
                    @Override
                    public double calculate(double x) {
                        return Math.log(x);
                    }

                    @Override
                    public double x_max() {
                        return 10.0;
                    }

                    @Override
                    public double y_max() {
                        return 2.30259;
                    }
                },
                new Equation("${\\tiny \\int_{0}^{2} x^{2} + \\sqrt[5]{x}\\, dx}$", 4.5812) {
                    @Override
                    public double calculate(double x) {
                        return Math.pow(x, 2.0) + Math.pow(x, 1.0/5.0);
                    }

                    @Override
                    public double x_max() {
                        return 2.0;
                    }

                    @Override
                    public double y_max() {
                        return 4.0 + Math.pow(2.0, 1.0/5.0);
                    }
                }
        };

        ArrayList<MonteCarloResult> equation_results = new ArrayList<>();

        for (Equation equation : equation_list){
            for(int num_times : number_of_elements) {
                // Approximate the area
                Double equation_answer = monte_carlo.calculate_area(equation, num_times);
                equation_results.add(new MonteCarloResult(equation, num_times, equation_answer));
            }
        }

        return equation_results;
    }

    /**
     * Method that is called in order to begin the monte carlo simulation and to display the results in a window.
     */
    public static void show(){
        TableView table = new TableView();
        String css_file = MonteCarloRunner.class.getResource("styles/javafx-tables.css").toExternalForm();
        table.getStylesheets().add(css_file);

        ObservableList<MonteCarloResult> table_data = FXCollections.observableArrayList();

        // Run the monte carlo algorithm
        ArrayList<MonteCarloResult> monteCarloResults = exec();

        // Add the results to the data that will be put into our table
        table_data.addAll(monteCarloResults);
        // Add the data to the table
        table.getItems().addAll(table_data);

        Stage monteCarloStage = new Stage();
        Scene scene = new Scene(new Group());

        // Setup the tabbed interface
        TabPane tabPane = new TabPane();
        tabPane.setPadding(new Insets(29, 0, 0, 0));
        tabPane.setSide(Side.RIGHT);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // We store which tab was open last so that when we 'Rerun' the simulation the window is opened up to the
        // tab that we were on before.
        tabPane.getSelectionModel().select(last_active_tab);

        // Create a menu which will be used to rerun our algorithm
        MenuBar menuBar = new MenuBar();
        menuBar.autosize();
        Menu file = new Menu("File");

        MenuItem rerun = new MenuItem("Rerun");
        rerun.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Before we close the program we want to save which tab is open so that when we restore the window
                // it gets opened up to the same tab.
                last_active_tab = tabPane.getSelectionModel().getSelectedIndex();
                monteCarloStage.close();
                // Recompute the results of the algorithm
                MonteCarloRunner.show();
            }
        });
        file.getItems().addAll(rerun);

        menuBar.getMenus().addAll(file);
        // Makes the menu bar have the same width as the window
        menuBar.prefWidthProperty().bind(monteCarloStage.widthProperty());


        BorderPane graphPanel = new BorderPane();
        FlowPane graphKey = new FlowPane();

        graphKey.setAlignment(Pos.CENTER);

        LineChart graph = populate_graph(graphKey, table_data);

        graphPanel.setCenter(graph);
        graphPanel.setBottom(graphKey);

        Tab tableTab = new Tab("Table data");

        // Create the table columns
        TableColumn[] tableColumns = create_table_columns();

        table.getColumns().addAll(tableColumns);
        tableTab.setContent(table);

        Tab graphTab = new Tab("Graph");
        graphTab.setContent(graphPanel);

        tabPane.getTabs().addAll(tableTab, graphTab);

        monteCarloStage.setTitle("Monte Carlo Algorithm - Area Under Curve");

        monteCarloStage.setWidth(width);
        monteCarloStage.setHeight(height);

        table.setPrefWidth(width);
        table.setPrefHeight(height);

        tabPane.prefWidthProperty().bind(monteCarloStage.widthProperty());

        monteCarloStage.heightProperty().addListener(
                (observable, oldValue, newValue) ->
                        tabPane.setPrefHeight(newValue.doubleValue() - 27)
        );

        ((Group) scene.getRoot()).getChildren().addAll(tabPane, menuBar);
        monteCarloStage.setScene(scene);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        monteCarloStage.setX((screenBounds.getWidth() - monteCarloStage.getWidth()) / 2);
        monteCarloStage.setX((screenBounds.getHeight() - monteCarloStage.getHeight()) / 4);
        monteCarloStage.show();
    }

    /**
     * Creates the columns that will be used in the table that displays the results of the simulation. The values
     * used in the columns are taken from the Properties on the MonteCarlo result.
     *
     * @return an array of the TableColumns created.
     */
    private static TableColumn[] create_table_columns(){
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

        TableColumn<MonteCarloResult, String> actual_result = new TableColumn<>("Exact Area");
        actual_result.setCellValueFactory(
                (param) ->
                        new SimpleStringProperty(create_formatted_string(param.getValue().equation.actual_area.getValue()))
        );
        actual_result.setPrefWidth(120);

        TableColumn<MonteCarloResult, String> error_percent = new TableColumn<>("Error %");
        error_percent.setCellValueFactory(
                (param) ->
                        new SimpleStringProperty(create_formatted_string(param.getValue().error_percentage.getValue()))
        );
        error_percent.setPrefWidth(120);

        return new TableColumn[]{equation, number_of_times, approx_result, actual_result, error_percent};
    }

    private static String create_formatted_string(Double number){
        return String.format("%.4f", number);
    }

    /**
     * Creates a line graph that represents the error percentage for each of the equations with respect to the number
     * of guesses taken.
     *
     * @param graphKey The panel that will house the equation images for reference.
     * @param table_data The results of the simulation
     * @return a line graph with each equation's results plotted.
     */
    private static LineChart populate_graph(FlowPane graphKey, ObservableList<MonteCarloResult> table_data){
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setAutoRanging(true);
        xAxis.setForceZeroInRange(false);
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Guesses");
        yAxis.setLabel("Error Percentage (%)");

        final LineChart<Number, Number> errorPercentageChart = new LineChart<>(xAxis, yAxis);
        errorPercentageChart.setTitle("Error Percentage Per Equation");
        int equation_count = 1;
        LinkedHashMap<Equation, ArrayList<HashMap<Integer, MonteCarloResult>>> equation_groups = create_equation_groups(table_data);
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

        return errorPercentageChart;
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

    /**
     * Creates a mapping where each equation is associated with a listing of all of the simulations run against it. The list
     * for each equation contains maps that associate the number of guesses used to the result of the equation.
     *
     * This gives us a data structure with the following structure:
     * {
     *     EquationObject1 : [
     *      {
     *         '1000': MonteCarloResult1000
     *      },
     *      {
     *         '10000': MonteCarloResult10000
     *      },
     *      ...
     *     ],
     *     EquationObject2 : [
     *      {
     *          '1000': MonteCarloResult2_1000
     *      },
     *      ...
     *     ],
     *     ...
     * }
     * @param table_data results from running the monte carlo algorithm on the equations
     * @return a LinkedHashMap of the format described above. We use a LinkedHashMap so that the order at which we
     * insert the values will be maintained, that way when we rerun the simulation the equations do not change order.
     */
    private static LinkedHashMap<Equation, ArrayList<HashMap<Integer, MonteCarloResult>>> create_equation_groups(ObservableList<MonteCarloResult> table_data){
        ArrayList<Equation> equations = get_equations(table_data);

        LinkedHashMap<Equation, ArrayList<HashMap<Integer, MonteCarloResult>>> equation_dict = new LinkedHashMap<>();
        // Loop through all of the equations
        for(int index = 0; index< equations.size(); index++){
            Equation equation = equations.get(index);

            ArrayList<HashMap<Integer, MonteCarloResult>> equation_dict_list = new ArrayList<>();
            // For each equation go through the data and grab the results from the equation
            for (int jPos = 0; jPos < table_data.size(); jPos++){

                MonteCarloResult result = table_data.get(jPos);
                HashMap<Integer, MonteCarloResult> result_dict = new HashMap<>();
                if (result.equation == equation) {
                    result_dict.put(result.num_times.getValue(), result);
                    // If our list of equation results does not contain the current result then we add it
                    if (!is_in(equation_dict_list, result_dict))
                        equation_dict_list.add(result_dict);
                }
            }
            // We have created our mapping for the equation so we add it to the dictionary that will be returned.
            equation_dict.put(equation, equation_dict_list);
        }

        return equation_dict;
    }

    /**
     * Helper method for determining if an ArrayList of Hashmaps contains the specified HashMap (result_dict).
     * @param equation_dict_list the array list of hash maps to check in
     * @param result_dict the hash map that should be searched for. For our use this is a dictionary with only one key
     *                    and value.
     * @return true if found, false otherwise
     */
    private static boolean is_in(ArrayList<HashMap<Integer, MonteCarloResult>> equation_dict_list, HashMap<Integer, MonteCarloResult> result_dict){
        for(HashMap<Integer, MonteCarloResult> map: equation_dict_list){
            Object[] result_key_array = result_dict.keySet().toArray();
            if(map.containsKey((Integer)result_key_array[0])){
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
    private static int last_active_tab;

    private static final int width = 1200;
    private static final int height = 850;
}
