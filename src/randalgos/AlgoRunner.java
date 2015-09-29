package randalgos;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * William Trent Holliday
 * 9/28/15
 */
public class AlgoRunner extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        MonteCarloRunner.show();
        RandomSelectRunner.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
