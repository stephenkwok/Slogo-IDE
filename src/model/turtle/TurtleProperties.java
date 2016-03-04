package model.turtle;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by rhondusmithwick on 2/22/16.
 *
 * @author Rhondu Smithwick
 */
public final class TurtleProperties {

    private static final int IMAGE_DIM = 30;

    private static final String DEFAULT_TURTLE_IMAGE = "resources/images/blackarrow.png";

    private final ImageView imageView = new ImageView();
    private final SimpleStringProperty image = new SimpleStringProperty(this, "turtleImage");
    private final SimpleObjectProperty<Dimension2D> imageDimensions = new SimpleObjectProperty<>(this, "imageDimensions");
    private final SimpleBooleanProperty visible = new SimpleBooleanProperty(this, "visible");
    private final SimpleObjectProperty<Point2D> location = new SimpleObjectProperty<>(this, "location");
    private final SimpleObjectProperty<Point2D> home = new SimpleObjectProperty<>(this, "home");
    private final SimpleDoubleProperty heading = new SimpleDoubleProperty(this, "heading");
    private final SimpleBooleanProperty penDown = new SimpleBooleanProperty(this, "penDown");
    private final SimpleStringProperty penColor = new SimpleStringProperty(this, "penColor");

    void init(Dimension2D turtleDispDimension) {
        setImage(DEFAULT_TURTLE_IMAGE);
        setVisible(true);
        home.set(new Point2D(turtleDispDimension.getWidth() / 2 - imageDimensions.get().getWidth() / 2,
                turtleDispDimension.getHeight() / 2 - imageDimensions.get().getHeight() / 2));
        setLocation(getHome());
        setHeading(180);
        setPenDown(true);
        setPenColor("black");
    }

    void addListeners() {
        visible.addListener((ov, oldVal, newVal) ->
                imageView.setVisible(newVal));
        location.addListener((ov, oldVal, newVal) -> {
            imageView.setTranslateX(newVal.getX());
            imageView.setTranslateY(newVal.getY());
        });
        image.addListener((ov, oldVal, newVal) -> {
            Image theImage = createImage(newVal);
            imageView.setImage(theImage);
            imageView.setFitHeight(IMAGE_DIM);
            imageView.setFitWidth(IMAGE_DIM);
            imageDimensions.set(new Dimension2D(IMAGE_DIM, IMAGE_DIM));
        });
    }

    public ImageView getImageView() {
        return imageView;
    }

    public SimpleStringProperty imageProperty() {
        return image;
    }

    private Image createImage(String filePath) {
        return new Image(filePath);
    }

    public void setImage(String image) {
        this.image.set(image);
    }

    public boolean getVisible() {
        return visible.get();
    }

    public void setVisible(boolean visible) {
        this.visible.set(visible);
    }

    public Point2D getLocation() {
        return location.get();
    }

    public void setLocation(Point2D location) {
        this.location.set(location);
    }

    public Point2D getHome() {
        return home.get();
    }

    public double getHeading() {
        return heading.get();
    }

    public void setHeading(double heading) {
        if (heading >= 360) {
            heading -= 360;
        } else if (heading < 0) {
            heading += 360;
        }
        this.heading.set(heading);
    }

    public boolean getPenDown() {
        return penDown.get();
    }

    public void setPenDown(boolean penDown) {
        this.penDown.set(penDown);
    }

    public String getPenColor() {
        return penColor.get();
    }

    public void setPenColor(String penColor) {
        this.penColor.set(penColor);
    }

    public SimpleStringProperty penColorProperty() {
        return penColor;
    }


}