package view.utilities;

import java.io.File;
import java.util.List;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.Size;

/**
 * abstract class which acts as a base for any pop ups shown by the view.
 * @author Cali
 *
 */
public abstract class PopUp {
	private int height;
	private int width; 
	private Stage s;
	private Scene myScene;
	private Group root;
	private VBox vBox;
	private String backgroundColor;
	
	/**
	 * Super constructor for a popup subclass instance
	 * @param height int height of window
	 * @param width int width of window
	 * @param backgroundColor css string for window background color
	 */
	public PopUp(int height, int width, String backgroundColor){
		this.height=height;
		this.width = width;
		this.backgroundColor=backgroundColor;
	}
	
	/**
	 * creates the popup and its various components then shows the window
	 */
	public void show(){
		s = new Stage();
		root = new Group();
		myScene = new Scene(root, height, width);
		createContainer();
		createScene();
		showScene();
	}

	/**
	 * creates the scene to be shown in the popup body
	 */
	protected abstract void createScene();

	
	private void createContainer() {
		vBox = new VBox(Size.POP_UP_PADDING.getSize());
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setStyle(backgroundColor);
        vBox.prefHeightProperty().bind(myScene.heightProperty());
        vBox.prefWidthProperty().bind(myScene.widthProperty());
        root.getChildren().add(vBox);
		
	}

	/**
	 * shows the scene to the user
	 */
	private void showScene() {
		s.setScene(myScene);
		s.show();
		
	}
	
	/**
	 * opens up a file chooser from the popup 
	 * @param fChoose fileChooser object to be opened
	 * @param save boolean whether chooser should be shown in open or save mode
	 * @return File choosen by user
	 */
	protected File showFChooser(FileChooser fChoose, boolean save){
		File file;
		if(save){
			file= fChoose.showSaveDialog(s);
		}else{
			file = fChoose.showOpenDialog(s);
		}
		return file;
		
	}
	
	/**
	 * closes the popup
	 */
	protected void closeScene(){
		s.close();
	}
	
	/**
	 * adds nodes to the popups scene in order
	 * @param nodeList list of nodes to be added
	 */
	protected void addNodes(List<Node> nodeList){
		vBox.getChildren().addAll(nodeList);
	}
	
	/**
	 * gets the size property of the popups created scene
	 * @param height boolean whether to return height or width
	 * @return height or width property of scene
	 */
	protected ReadOnlyDoubleProperty getSize(boolean height){
		if(height){
			return myScene.heightProperty();
		}
		return myScene.widthProperty();
	}
	
	/**
	 * sets the popup stage's title
	 * @param title string title to set
	 */
	protected void setStageTitle(String title){
		s.setTitle(title);
	}
	
	/**
	 * hides the popups stage from the user
	 */
	protected void hideScene(){
	    s.hide();
	}
	
	
}
