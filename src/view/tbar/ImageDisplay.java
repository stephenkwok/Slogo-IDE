package view.tbar;

import java.util.Arrays;
import java.util.Map.Entry;

import view.Size;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import observables.MapObservable;

public class ImageDisplay extends PaletteDisp{

    private MapObservable<Integer, String> iMap;

    
    public ImageDisplay (String title, SimpleStringProperty error, MapObservable<Integer, String> iMap) {
        super(title, error);
        this.iMap=iMap;
   
       
        
    }

    @Override
    protected void addToPalette (Entry<Integer, String> e) {
        setHBox();
        Label title = createLabel("index", e.getKey().toString() );
        Image image = new Image(e.getValue());
        ImageView disp = new ImageView();
        disp.setFitHeight(Size.PALETTE_DIM.getSize());
        disp.setFitWidth(Size.PALETTE_DIM.getSize());
        disp.setImage(image);
        addNodesToHBox(Arrays.asList(title));
        addImagesToHBox(Arrays.asList(disp));
        
        
    }
    
    
    @Override
    public void createScene(){
        super.createScene();
		iMap.getEntrySet().stream().forEach(e-> addToPalette(e));
    }



    

}
