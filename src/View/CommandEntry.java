package View;

import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import Observables.ObjectObservable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * This class implements the CommandEntryInterface interface and allows the user to input
 * commands to be executed when execute button (within View class) is clicked
 *
 * @author Stephen
 */

public class CommandEntry implements CommandEntryInterface, Observer {

    private static final int SCROLL_TBOX_DIFF = 2;
	private static final int TITLE_SPACE = 25;
    private static final String DEFAULT_LOCATION = "resources/guiStrings/";
    private static final String DISP = "disp";
    private static final String DEFAULT_LANGUAGE = "English";
    private static final String NEW_LINE = "\n";
    private static final String SHOW_IN_BOX = "show in text box";
    private static final int STARTING_WIDTH = 200;
    private final ObjectObservable<String> input, intCommands, commHistory;


    private TextArea myEntryBox;
    private ScrollPane myScrollPane;
    private VBox container;
    private Label title;
    private String dispLang;
    private ResourceBundle myResources;

    public CommandEntry(ObjectObservable<String> input, ObjectObservable<String> intCommands, ObjectObservable<String> commHistory) {
        this.dispLang = DEFAULT_LANGUAGE;
        this.myResources = ResourceBundle.getBundle(DEFAULT_LOCATION + dispLang + DISP);
        this.input = input;
        this.commHistory = commHistory;
        this.intCommands = intCommands;
        intCommands.addObserver(this);
        setScrollPane();
        container = new VBox();
        createTitle();
        createTextBox();
        myScrollPane.setContent(container);
        
        
    }

    private void setScrollPane () {
        myScrollPane = new ScrollPane();
        myScrollPane.setMinViewportWidth(STARTING_WIDTH);
        myScrollPane.setPrefViewportWidth(STARTING_WIDTH);
        myScrollPane.setMaxWidth(STARTING_WIDTH);
        VBox.setVgrow(myScrollPane, Priority.SOMETIMES);
    }

    private void createTextBox () {
        myEntryBox = new TextArea();
        myEntryBox.prefHeightProperty().bind(myScrollPane.heightProperty().subtract(TITLE_SPACE));
        myEntryBox.prefWidthProperty().bind(myScrollPane.widthProperty().subtract(SCROLL_TBOX_DIFF));
        container.getChildren().add(myEntryBox);
    }

    private void createTitle () {
        title = new Label(myResources.getString("entryTitle"));
        container.getChildren().add(title);
        container.setAlignment(Pos.TOP_CENTER);

    }

    @Override
    public Node getNode() {
        return myScrollPane;
    }


    private void getCommandsFromString(String text) {
        input.set(text);
    }
    
   private void passInternalCommands(String command, boolean showInTextBox) {
        if(showInTextBox){
            String curr = myEntryBox.getText();
            if(!curr.endsWith(NEW_LINE) && !curr.equals("")){
            	curr = curr + NEW_LINE + command;
            }else{
            	curr = curr + command;
            }
            myEntryBox.setText(curr);
        }else{
            getCommandsFromString(command);
        }
    }

   @Override
    public void processCommands() {
        String text = myEntryBox.getText();
        commHistory.set(text);
        getCommandsFromString(text);
        myEntryBox.clear();
    }

    @Override
    public void update (Observable o, Object arg) {

        String command = intCommands.get();
        boolean show =command.startsWith(SHOW_IN_BOX);
        if(show){
            
            command = command.substring(SHOW_IN_BOX.length());
        }
        passInternalCommands(command, show);
        
    }


}
