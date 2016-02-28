package Model.Movement;

import Model.TreeNode.TurtleCommandNode;
import Model.Turtle.TransitionAction;
import Model.Turtle.Turtle;
import Model.Turtle.TurtleAction;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;

/**
 * Created by rhondusmithwick on 2/23/16.
 *
 * @author Rhondu Smithwick
 */
abstract class Movement extends TurtleCommandNode {

    double move(int direction) {
        Turtle myTurtle = getTurtle();
        double distance = getChildren().get(0).getValue();
        Line penLine = new Line();
        myTurtle.getGroup().getChildren().add(penLine);
        ImageView imageView = myTurtle.getView();
        TurtleAction action = new TransitionAction(myTurtle, imageView, penLine, distance, direction);
        addAction(action);
        return distance;
    }

    @Override
    public int getNumChildrenRequired() {
        return 1;
    }
}
