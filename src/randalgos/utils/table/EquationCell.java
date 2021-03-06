package randalgos.utils.table;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.image.ImageView;

import javafx.scene.layout.VBox;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import randalgos.result.MonteCarloResult;
import randalgos.utils.EquationImageBuilder;

import java.awt.*;

/**
 * William Trent Holliday
 * 9/26/15
 *
 * Custom table cell that will create an image based on the math equation string for that cell value.
 * Uses http://forge.scilab.org/index.php/p/jlatexmath/ to create the formula images.
 */
public class EquationCell extends TableCell<MonteCarloResult, String> {
    VBox box;
    ImageView mathImage;

    public EquationCell() {
        box = new VBox();
        box.setAlignment(Pos.CENTER);

        mathImage = new ImageView();
        box.getChildren().addAll(mathImage);

        setGraphic(box);
    }

    @Override
    public void updateItem(String math, boolean empty){
        if (math != null) {
            mathImage.setImage(EquationImageBuilder.create_image(math));
        }
    }
}
