package view.tbar;

import view.tbar.popupdisplays.ImageChooser;
import view.tbar.popupdisplays.IndexMapSaver;
import view.tbar.popupdisplays.WorkSpaceSaver;
import view.utilities.PopUp;
import java.util.Observable;
import java.util.Observer;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ComboBox;
import maps.ColorMap;
import maps.ImageMap;
import observables.ObjectObservable;

/**
 * class represents the bottom sub bar of the tool bar. it is a sub class of the
 * abstract class sub bar.
 * 
 * @author calisnelson and Stephen Kwok
 *
 */
public class BottomBar extends SubBar implements Observer {


	private ObjectObservable<String> backgroundColor;
	private ColorMap colorMap;
	private ImageMap imageMap;
	private ComboBox<String> backgroundColorBox;
	private ComboBox<String> penColorBox;

	private SimpleStringProperty image;

	/**
	 * Creates a new bottom bar instance
	 * 
	 * @param language
	 *            language string observable for setting and storing parsing
	 *            language
	 * @param intCommand
	 *            string observable for passing commands to command entry
	 *            instance
	 * @param cMap
	 *            Index map object for mapping colors to integer indexes
	 * @param iMap
	 *            Index map object for mapping images to integer indexes
	 */
	public BottomBar(ObjectObservable<String> language, ObjectObservable<String> internalCommand, ColorMap colorMap,
			ImageMap imageMap, SimpleStringProperty image, ObjectObservable<String> backgroundColor) {
		super(language, internalCommand, colorMap);
		this.imageMap = imageMap;
		this.colorMap = colorMap;
		this.colorMap.getIndexMap().addObserver(this);
		this.image =image;
		
		this.backgroundColor = backgroundColor;
		
	}

	/**
	 * creates all comboboxes needed for sub bar
	 */
	@Override
	protected void createComboBoxes() {
		
		backgroundColorBox = createComboBox("bColor", getColors(), e -> setBackground());
		penColorBox = createComboBox("pColor", getColors(), e -> setPColor());

	}
	
	private void setPColor() {
		String pColor = penColorBox.getSelectionModel().getSelectedItem();
		String command = getCommand("SetPenColor");
		int index = getColorIndex(pColor);
		passCommand(command + " " + index);

	}

	private void setBackground() {
		String bColor = backgroundColorBox.getSelectionModel().getSelectedItem();
		backgroundColor.set(bColor.toLowerCase());

	}
	



	/**
	 * creates all buttons needed for sub bar
	 */
	@Override
	protected void createButtons() {
		makeButton("image", e -> chooseTurtIm());
		makeButton("workSaver", e -> saveWorkSpace());
		makeButton("saveColor", e -> saveMap(true));
		makeButton("saveImage", e -> saveMap(false));

	}

	private void saveMap(boolean colors) {
		try {
			PopUp mapSave;
			if (colors) {
				mapSave = new IndexMapSaver(colorMap);
			} else {
				mapSave = new IndexMapSaver(imageMap);
			}
			mapSave.show();
		} catch (Exception e) {
			return;
		}
	}
	
	private void chooseTurtIm() {

		ImageChooser imChoose = new ImageChooser();
		imChoose.show();
		String newImage;
		try {
			newImage = imChoose.getChosen();
			if (newImage != null) {
				image.set(newImage);
			}
		} catch (Exception e) {
			return;
		}
	}

	private void saveWorkSpace() {
		PopUp workspaceSaver = new WorkSpaceSaver(getColors(), getLanguages());
		workspaceSaver.show();
	}
	
	/**
	 * called whenever color map is updated with new option. Removes and
	 * recreates background color box and pen color box so that that they
	 * include the new option.
	 */
	@Override
	public void update(Observable o, Object arg) {
		getContainer().getChildren().removeAll(backgroundColorBox, penColorBox);
		createComboBoxes();
	}

}
