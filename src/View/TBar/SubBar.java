package View.TBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.ResourceBundle;
import Maps.ColorMap;
import Observables.ObjectObservable;
import View.Defaults;
import View.Size;
import View.utilities.ComboFactory;
import View.utilities.FileGetter;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public abstract class SubBar implements Observer {

    private ResourceBundle myResources, myCommands;
    private HBox container;
    private ObjectObservable<String> language;
    private static final char SPLITTER = '|'; 
    private SimpleStringProperty error;
    private ColorMap colors;


    public SubBar(ObjectObservable<String> language, SimpleStringProperty error){
        this.language=language;
        this.error=error;
        myResources = ResourceBundle.getBundle(Defaults.DISPLAY_LOC.getDefault());
        initHBox();
        getColorMap();
        setParsingLanguage(language.get());
        createButtons();
        createComboBoxes();
    }


    protected void setParsingLanguage(String pLang){

        myCommands = ResourceBundle.getBundle( pLang);

        language.set(pLang);
    }

    public void initHBox(){
        container = new HBox(Size.TB_PADDING.getSize());
        container.setAlignment(Pos.CENTER);

    }

    protected void showError(String key){
        error.set("");
        error.set(myResources.getString(key));
    }

    public Button makeButton(String key, EventHandler<ActionEvent> handler) {
        Button newButt = new Button(myResources.getString(key));
        container.getChildren().add(newButt);
        newButt.setOnAction(handler);
        HBox.setHgrow(newButt, Priority.ALWAYS);
        return newButt;
    }

    protected String getCommand(String key) {
        String retrievedString = myCommands.getString(key);
        int splitterPos = retrievedString.indexOf(SPLITTER);
        if (splitterPos > 0) {
            return retrievedString.substring(0, splitterPos);
        } else {
            return retrievedString;
        }
    }
    
    protected ComboBox<String> createComboBox(String key, List<String> choices, EventHandler<ActionEvent> handler){
    	ComboBox<String> cBox = ComboFactory.createBox(myResources.getString(key), choices, handler);
    	HBox.setHgrow(cBox, Priority.ALWAYS);
    	container.getChildren().add(cBox);
    	return cBox;
    	
    }

    private void getColorMap() {
        try {
            this.colors = ColorMap.getInstance();
            colors.getIndexMap().addObserver(this);
        } catch (Exception e) {
            error.set("");
            error.set(myResources.getString("colorError"));
        }

    }

    protected List<String> getLanguages() {
        return FileGetter.getAllFromDirectory(Defaults.PARSELANG_LOC.getDefault());
    }

    protected List<String> getColors(){
        try {
            return new ArrayList<String>(colors.getIndexMap().getValues());
        }
        catch (Exception e) {
            error.set("");
            error.set(myResources.getString("colorError"));
        }
        return null;

    }


    public HBox getContainer(){
        return container;
    }

    protected abstract void createComboBoxes();
    protected abstract void createButtons();



}