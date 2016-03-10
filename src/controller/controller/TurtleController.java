package controller.controller;

import controller.slogoparser.ExpressionTree;
import controller.slogoparser.SlogoParser;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Dimension2D;
import javafx.scene.Group;
import model.deprecated.Command;
import model.treenode.TreeNode;
import model.turtle.Turtle;
import model.usercontrol.MakeUserInstruction;
import observables.MapObservable;
import observables.ObjectObservable;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

/**
 * Created by rhondusmithwick on 2/22/16.
 *
 * @author Rhondu Smithwick
 */
public class TurtleController implements Controller, Observer {

    private static final String DEFAULT_LANGUAGE = "resources/languages/English";

    private final SlogoParser parser = new SlogoParser("resources/languages/Syntax");

    private final Group group = new Group();
    private final Turtle myTurtle;
    private final ObjectObservable<String> language = new ObjectObservable<>();
    private final ObjectObservable<String> input = new ObjectObservable<>();
    private ObjectObservable<String> backgroundColor = new ObjectObservable<>();
    
    private final SimpleStringProperty error = new SimpleStringProperty(this, "error");

    private final MapObservable<String, TreeNode> variables = new MapObservable<>("variables");

    private final MapObservable<String, MakeUserInstruction> definedCommands = new MapObservable<>("definedCommands");
    
    private MapObservable<Integer, String> colorMap = new MapObservable<>("colorMap");
    private MapObservable<Integer, String> imageMap = new MapObservable<>("imageMap");

    public TurtleController(Dimension2D turtleDispDimension) {
        myTurtle = new Turtle(turtleDispDimension);
        language.addObserver(this);
        input.addObserver(this);
        language.set(DEFAULT_LANGUAGE);
        variables.addObserver(this);
        definedCommands.addObserver(this);
        group.getChildren().add(myTurtle.getGroup());
    }

    public ObjectObservable<String> getLanguage() {
        return language;
    }

    public ObjectObservable<String> getInput() {
        return input;
    }

    @Override
    public void takeInput(String input) {
        Queue<Entry<String, String>> parsedText = parser.parseText(input);
        runCommands(parsedText);
    }
    
    private void runCommands(Queue<Entry<String, String>> parsedText) {
        if (parsedText == null) {
            error.set("");
            error.set("Command not recognized: " + input);
        } else {
            try {
                ExpressionTree expressionTree = new ExpressionTree(myTurtle, variables, definedCommands, colorMap, imageMap, backgroundColor, parsedText);
                new Thread(expressionTree::executeAll).start();
            } catch (Exception es) {
                error.set("");
                error.set("Exception in command argument: " + input);
            }
        }
    }
    
    @Override
    public List<Command> getCommands() {
        return null;
    }

    @Override
    public Group getGroup() {
        return group;
    }
    
    public void setColorMap(MapObservable<Integer, String> map) {
    	this.colorMap = map;
    }
    
    public void setImageMap(MapObservable<Integer, String> map) {
    	this.imageMap = map;
    }
    
    public void setBackgroundColor(ObjectObservable<String> color) {
    	this.backgroundColor = color;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == language) {
            parser.addPatterns(language.get());
        }
        if (o == input) {
            takeInput(input.get());
        }
    }

    @Override
    public List<SimpleStringProperty> getProperties() {
        return Arrays.asList(
        		error,
                myTurtle.getTurtleProperties().imageProperty(),
                myTurtle.getTurtleProperties().penColorProperty(),
                variables.getStringProperty(), definedCommands.getStringProperty());
    }
}
