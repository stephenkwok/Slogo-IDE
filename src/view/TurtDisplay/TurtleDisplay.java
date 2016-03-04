package view.turtdisplay;

import java.util.Observable;
import java.util.Observer;
import Observables.ObjectObservable;
import view.Defaults;
import javafx.geometry.Dimension2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class TurtleDisplay implements Observer {
    private static final double SCROLL_BAR_INITIAL = .5;
    private Rectangle background;
    private Group dispArea;
    private ScrollPane scroll;
    private ObjectObservable<String> bgColor;

    public TurtleDisplay(ObjectObservable<String> bgColor, Dimension2D turtleDispDimension) {
        this.bgColor=bgColor;
        bgColor.addObserver(this);
        background = new Rectangle(turtleDispDimension.getWidth(), turtleDispDimension.getHeight());
        dispArea = new Group();
        setBackground(Defaults.TURT_BACKGROUND.getDefault());
        dispArea.getChildren().add(background);
        setScrollPane();

    }


    private void setScrollPane () {
        scroll = new ScrollPane();
        scroll.setVvalue(SCROLL_BAR_INITIAL);
        scroll.setHvalue(SCROLL_BAR_INITIAL);
        scroll.setContent(dispArea);

       
    }

    private void setBackground(String color) {
        background.setFill(Color.web(color));

    }


    public Node getTurtlePane() {
        return scroll;
    }


    public Group getTurtleArea() {
        return dispArea;
    }


    @Override
    public void update (Observable o, Object arg1) {
        String color = bgColor.get();
        setBackground(color);
        
    }


}