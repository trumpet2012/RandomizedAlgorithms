package randalgos.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import java.awt.Color;
import java.io.File;

/**
 * William Trent Holliday
 * 9/28/15
 */
public class EquationImageBuilder {

    public static Image create_image(String math){
        Image mathImage;
        String file_name = "/home/trent/IdeaProjects/RandomizedAlgorithms/src/" + math + "_image.png";
        File image_file = new File(file_name);
        TeXFormula formula = new TeXFormula(math);
        if(!image_file.exists()) {
            formula.createPNG(TeXConstants.UNIT_MU, 100, file_name, new Color(255, 255, 255, 255), Color.black);
        }
        mathImage = new Image("file:" + file_name);

        return mathImage;
    }

}
