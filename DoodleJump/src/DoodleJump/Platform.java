package DoodleJump;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class contains the Platforms' shape to be added the animation pane as
 * well as methods for setting and getting its position. A method to remove a 
 * Platform from a passed in pane is here.
 */
public class Platform {
	private Rectangle _structure;

	/**
	 * The constructor where the Platform's shape is instantiated, its
	 * specifications are set, and it is added to the passed in pane.
	 */
	public Platform(Pane pane) {
		_structure = new Rectangle();
		_structure.setWidth(Constants.PLATFORM_WIDTH);
		_structure.setHeight(Constants.PLATFORM_HEIGHT);
		_structure.setFill(Color.GREY);
		pane.getChildren().add(_structure);
	}

	/**
	 * Gets the y-coordinate of a Platform.
	 */
	public double getCurY() {
		return _structure.getY();
	}

	/**
	 * Sets the y-coordinate of a Platform.
	 */
	public void setCurY(double y) {
		_structure.setY(y);
	}

	/**
	 * Gets the x-coordinate of a Platform.
	 */
	public double getCurX() {
		return _structure.getX();
	}

	/**
	 * Sets the x-coordinate of a Platform.
	 */
	public void setCurX(double x) {
		_structure.setX(x);
	}

	/**
	 * Removes the Platform's shape from the passed-in pane.
	 */
	public void removePlatform(Pane pane) {
		pane.getChildren().remove(_structure);
	}
}
