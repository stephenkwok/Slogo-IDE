package View.TBar;

import Observables.ObjectObservable;
import View.Defaults;
import View.Size;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ToolBar{

    private static final String GIF_EXT = "*.gif";
    private static final String GIF = "GIF";
    private static final String JPG_EXT = "*.jpg";
    private static final String JPG = "JPG";

    private static final String PNG_EXT = "*.png";
    private static final String PNG = "PNG";

  

    private final ObjectObservable<String> language, bgColor;
    private SimpleStringProperty image, penColor, error;
    private HBox container;
    private HelpScreen hScreen;
    private ResourceBundle myResources;
    private String dispLang, bColor, pLanguage, pColor;
    private ArrayList<String> parseLangs, possColors;
    private ComboBox<String> langBox, bColorBox, pColorBox;

    public ToolBar(ObjectObservable<String> language, SimpleStringProperty error, ObjectObservable<String> bgColor, 
                   SimpleStringProperty image, SimpleStringProperty penColor) {
        hScreen = new HelpScreen();
        this.image=image;
        this.penColor=penColor;
        this.language = language;
        this.error = error;
        this.bgColor = bgColor;
        this.dispLang = Defaults.DISPLAY_LANG.getDefault();
        myResources = ResourceBundle.getBundle(Defaults.DISPLAY_LOC.getDefault() + dispLang);
        setHBox();
        createButtons();
        getLanguages();
        getColors();
        createComboBoxes();
    }



    private void setHBox () {
        container = new HBox();
        container.setAlignment(Pos.CENTER);
        container.setSpacing(Size.TB_PADDING.getSize());
    }

    public HBox getToolBarMembers() {

        return container;
    }

    private void createComboBoxes() {
        langBox = createBox("selLang", parseLangs, e -> setLang());
        bColorBox = createBox("bColor", possColors, e -> setBackground());
        pColorBox = createBox("pColor", possColors, e -> setPColor());
    }


    private void setPColor() {
        pColor = pColorBox.getSelectionModel().getSelectedItem();
        penColor.set(pColor.toLowerCase());

    }

    private void setBackground() {
        bColor = bColorBox.getSelectionModel().getSelectedItem();
        bgColor.set(bColor.toLowerCase());
    }

    private void setLang() {
        pLanguage = Defaults.PARSELANG_LOC.getDefault() + langBox.getSelectionModel().getSelectedItem();
        language.set(pLanguage);
    }

    private ComboBox<String> createBox(String label, ArrayList<String> choices, EventHandler<ActionEvent> handler) {
        ComboBox<String> comBox = new ComboBox<>();
        comBox.setPromptText(myResources.getString(label));
        for (String choice : choices) {
            comBox.getItems().add(choice);
        }
        comBox.setOnAction(handler);
        HBox.setHgrow(comBox, Priority.ALWAYS);
        container.getChildren().add(comBox);
        return comBox;

    }

    @SuppressWarnings("rawtypes")
    private void getColors() {
        try{
            possColors = new ArrayList<>();
            Class colorClass = Class.forName(Defaults.FX_PAINT_CLASS.getDefault());
            Field[] fields = colorClass.getFields();
            for (Field field : fields) {
                Object o = field.get(null);
                if (o instanceof Color) {
                    possColors.add(field.getName());
                }
            }
        }catch (Exception e) {
        	error.set("");
            error.set(myResources.getString("colorError"));
        }

    }

    private void getLanguages() {
        parseLangs = new ArrayList<>();
        File directory = new File(Defaults.PARSELANG_LOC.getDefault());
        File[] fList = directory.listFiles();
        for (File file : fList) {
            String name = file.getName();
            parseLangs.add(name.substring(0, name.lastIndexOf('.')));
        }
    }

    private void makeButton(String label, EventHandler<ActionEvent> handler) {
        Button newButt = new Button();
        newButt.setText(label);
        container.getChildren().add(newButt);
        newButt.setOnAction(handler);
        HBox.setHgrow(newButt, Priority.ALWAYS);
    }


    private void createButtons() {
        makeButton(myResources.getString("help"), e -> hScreen.showHelpScreen(myResources.getString("helpFile")));
        makeButton(myResources.getString("image"), e -> chooseTurtIm());
    }


    private void chooseTurtIm() {
        FileChooser fChoose = new FileChooser();
        Stage s = new Stage();
        setUpFileChooser(fChoose, s);
        File file = fChoose.showOpenDialog(s);
        s.close();
        if (file == null) {
            return;
        }
        try {
            String imagepath = file.toURI().toURL().toString();
            image.set(imagepath);


        } catch (MalformedURLException e) {
        	error.set("");
            error.set(myResources.getString("picError"));
        }
    }

    private void setUpFileChooser(FileChooser fChoose, Stage s) {
        Group root = new Group();
        s.setScene(new Scene(root, Size.MINI.getSize(), Size.MINI.getSize()));

        fChoose.setTitle(myResources.getString("getFile"));
        fChoose.getExtensionFilters().addAll(new ExtensionFilter(PNG, PNG_EXT),
                                             new ExtensionFilter(JPG, JPG_EXT), 
                                             new ExtensionFilter(GIF, GIF_EXT));
        s.show();
        s.hide();
    }



}
