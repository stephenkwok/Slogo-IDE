package view;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import controller.controller.Controller;
import javafx.geometry.Dimension2D;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import observables.ObjectObservable;
import main.Slogo;

public class MultiView {
    private static TabPane tPane;
    private static ArrayList<ViewInt> views;
    private static Dimension2D tDisp;
    private static ObjectObservable<String> input;
    private static ObjectObservable<String> pLang;
    private static ResourceBundle myResources;
    private static Controller controller;

    public MultiView(Dimension2D turtleDispDimension, ObjectObservable<String> input, ObjectObservable<String> pLang, Controller controller){
        MultiView.myResources=ResourceBundle.getBundle(Defaults.DISPLAY_LOC.getDefault());
        MultiView.input=input;
        MultiView.tDisp=turtleDispDimension;
        MultiView.pLang=pLang;
        tPane = new TabPane();
        views = new ArrayList<>();
        MultiView.controller=controller;

    }

    public static ViewInt createTab(){
        ViewInt v = new View(tDisp,  input,  pLang);
        views.add(v);
        v.getInnerGroup().getChildren().add(controller.getGroup());
        v.bindSize(tPane);
        Slogo.bindProperties(v);
        Tab t = new Tab();
        t.setText(myResources.getString("workSpace"));
        t.setContent(v.getGroup());
        tPane.getTabs().add(t);
        return v;
    }

    public List<ViewInt> getViews(){
        return views;
    }

    public TabPane getPane(){
        return tPane;
    }

    public void bindSize(Scene s){
        tPane.prefHeightProperty().bind(s.heightProperty());
        tPane.prefWidthProperty().bind(s.widthProperty());
    }

}
