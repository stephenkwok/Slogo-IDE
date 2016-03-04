package view.envdisplay;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import observables.ObjectObservable;

public class MethodDisplay extends DefinedObjectsDisplay {

	private EnvUpdate updater;

	public MethodDisplay(ObjectObservable<String> parsingLanguage, ObjectObservable<String> intCommand,
			SimpleStringProperty methods, SimpleStringProperty error) {
		super(methods, parsingLanguage, intCommand, error);
		setDisplayTitle("methodDisplayTitle");

		createCurrEnvDisp();
	}

	@Override
	protected void updateDefinedObject(Label label) {
		updater = getUpdater("View.EnvDisplay.MethodUpdate");
		updater.updateEnv(label);
	}
}