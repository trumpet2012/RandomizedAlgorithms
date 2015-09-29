package randalgos;

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
import randalgos.utils.AlgoHelper;

import java.util.ArrayList;

/**
 * William Trent Holliday
 * 9/28/15
 */
public class RandomSelectRunner {

    private static void exec(){
        RandomSelect random_select = new RandomSelect();

        ArrayList<AlgoResult> results = new ArrayList<>(number_of_elements.length);

        for (int i = 0; i < number_of_elements.length; i++){
            int[] arr = AlgoHelper.rand_array(number_of_elements[i]);
            results.add(random_select.execute(arr, RandomSelectRunner.position));
        }
        table_data.addAll(results);
    }

    public static void show() {
        exec();
        Stage randomSelectStage = new Stage();
        Scene scene = new Scene(new Group());

        int width = 350;
        int height = 250;

        randomSelectStage.setTitle("Random Select Algorithm");

        randomSelectStage.setWidth(width);
        randomSelectStage.setHeight(height);
        table.setPrefWidth(width);
        table.setPrefHeight(height);

        randomSelectStage.widthProperty().addListener(
                (observable, oldValue, newValue) ->
                        table.setPrefWidth(newValue.doubleValue())
        );

        randomSelectStage.heightProperty().addListener(
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
        int position = RandomSelectRunner.position;
        String position_suffix = "th";
        if (position == 1){
            position_suffix = "st";
        } else if(position == 2){
            position_suffix = "nd";
        }else if (position == 3){
            position_suffix = "rd";
        }

        TableColumn<AlgoResult, Integer> result = new TableColumn<>(position + position_suffix + " smallest");
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

        randomSelectStage.setScene(scene);
        randomSelectStage.show();
    }

    private static final int position = 3; // Third smallest
    private static final int[] number_of_elements = {1000, 10000, 100000, 1000000};
    private static final TableView table = new TableView();
    private static ObservableList<AlgoResult> table_data = FXCollections.observableArrayList();

}
