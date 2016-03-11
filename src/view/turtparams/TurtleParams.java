package view.turtparams;

import java.util.ResourceBundle;

import view.Defaults;
import view.Size;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Class is responsible for displaying certain turtle parameters and properties to the user such as location, 
 * heading, etc.
 * @author Stephen Kwok
 *
 */

public class TurtleParams {



    private ResourceBundle myResources;
    private ScrollPane scroll;

    private Label title;
    private VBox box;

    /**
     * creates new turtle params object
     * @param location SimpleObjectProperty containing turtle's location
     * @param heading SimpleDoubleProperty containing turtle's heading
     * @param penDown SimpleBooleanProperty showing whether turtle's pen is down
     * @param penColor SimpleStringProperty displaying turtle's pen color
     */
    public TurtleParams() {


        myResources = ResourceBundle.getBundle(Defaults.DISPLAY_LOC.getDefault());
        setScroll();

        box = new VBox();
        box.prefWidthProperty().bind(scroll.widthProperty());
        scroll.setContent(box);
        addTitle();
        addParams();
    }

    private void addParams () {
        box.getChildren().add(createLabel(myResources.getString("penLoc")));
        box.getChildren().add(createLabel(myResources.getString("penHead")));
        box.getChildren().add(createLabel(myResources.getString("penStat")));
        box.getChildren().add(createLabel(myResources.getString("penColor")));
    }

    private void setScroll() {
        scroll = new ScrollPane();
        scroll.setMinViewportHeight(Size.BOTTOM_HEIGHT.getSize());
        scroll.setPrefViewportHeight(Size.BOTTOM_HEIGHT.getSize());
        scroll.setMaxHeight(Size.BOTTOM_HEIGHT.getSize());
        HBox.setHgrow(scroll, Priority.ALWAYS);
    }


    private void addTitle() {
        title = createLabel(myResources.getString("TurtlePropertiesTitle"));
        box.getChildren().add(title);
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setAlignment(Pos.TOP_CENTER);
        label.setStyle(Defaults.BORDER_COLOR.getDefault());
        label.prefWidthProperty().bind(scroll.widthProperty());
        label.setWrapText(true);
        return label;
    }
    
    /**
     * returns the scrollpane containing all the nodes needed to display the turtle parameters
     * @return scroll pane containing all components needed to display parameters
     */
    public Node getTurtleParams() {
        return scroll;
    }

}
