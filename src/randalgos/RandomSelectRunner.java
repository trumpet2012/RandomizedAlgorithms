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

import randalgos.math.RandomSelect;
import randalgos.result.AlgoResult;
import randalgos.result.MonteCarloResult;
import randalgos.utils.AlgoHelper;
import randalgos.utils.table.EquationCell;

import java.util.ArrayList;

/**
 * William Trent Holliday
 * 9/28/15
 */
public class RandomSelectRunner extends Application {

    public static void main(String[] args){
        RandomSelect random_select = new RandomSelect();

        ArrayList<AlgoResult> results = new ArrayList<>(number_of_elements.length);

        for (int i = 0; i < number_of_elements.length; i++){
            int[] arr = AlgoHelper.rand_array(number_of_elements[i]);
            results.add(random_select.execute(arr, RandomSelectRunner.position));
        }
        table_data.addAll(results);

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new Group());

        int width = 350;
        int height = 250;

        primaryStage.setTitle("Random Select Algorithm");

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

        TableColumn<AlgoResult, Integer> num_items = new TableColumn<>("Num of items");
        num_items.setCellValueFactory(
                (param) ->
                        param.getValue().number_of_items.asObject()
        );

        TableColumn<AlgoResult, Long> exec_time = new TableColumn<>("Exec. Time (ns)");
        exec_time.setCellValueFactory(
                (param) ->
                        param.getValue().exec_time.asObject()
        );
        String position_suffix = "th";
        if (this.position == 1){
            position_suffix = "st";
        } else if(this.position == 2){
            position_suffix = "nd";
        }else if (this.position == 3){
            position_suffix = "rd";
        }

        TableColumn<AlgoResult, Integer> result = new TableColumn<>(this.position + position_suffix + " smallest");
        result.setCellValueFactory(
                (param) ->
                        param.getValue().result.asObject()
        );

        table.setItems(table_data);
        table.getColumns().addAll(num_items, exec_time, result);

        final VBox vbox = new VBox();
        vbox.setSpacing(100);
        vbox.setPadding(new Insets(0, 0, 0, 0));
        vbox.getChildren().addAll(table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static final int position = 3; // Third smallest
    private static final int[] number_of_elements = {1000, 10000, 100000, 1000000};
    private final TableView table = new TableView();
    private static ObservableList<AlgoResult> table_data = FXCollections.observableArrayList();

}
