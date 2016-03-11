package view.tbar.popupdisplays;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import observables.ObjectObservable;

/**
 * This class provides a platform that allows users to select a turtle and make
 * it active. It is a subclass of the abstract popup class
 * 
 * @author Stephen Kwok
 *
 */

public class TurtleSelector extends TurtlePropertyUpdater {
	

	public TurtleSelector(SimpleStringProperty turtleIDs,
			ObjectObservable<String> internalCommand, ObjectObservable<String> parsingLanguage) {
		super(turtleIDs, internalCommand, parsingLanguage, "turtleSelectTitle");
	}

	@Override
	protected void createElementsBelowCheckBoxes() {
		addToScene(new Label(getStringFromResources("turtleSelectClickButtonPrompt")));
	}

	@Override
	protected String makeCommand(String turtleIDs) {
		String tellCommand = translateCommand("Tell");
		return tellCommand + " [ " + turtleIDs + "]";
	}

}
