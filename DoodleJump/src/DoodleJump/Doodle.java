package DoodleJump;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class contains the Doodle's shape to be added  the animation pane as
 * well as methods for setting and getting its velocity and position. A method
 * to check if it is intersecting with a node, in this case Platform's shape, is
 * also here.
 */
public class Doodle {
	private Rectangle _shape;
	private double _velocity;

	/**
	 * The constructor where the Doodle's shape is created, this shape is added
	 * to the pane passed in, and its velocity is stored as 0 by default.
	 */
	public Doodle(Pane pane) {
		this.createDoodle();
		pane.getChildren().add(_shape);
		_velocity = 0;
	}

	/**
	 * This method instantiates and sets the dimensional and positional
	 * specifications of the Doodle's shape.
	 */
	public void createDoodle() {
		_shape = new Rectangle();
		_shape.setWidth(Constants.DOODLE_WIDTH);
		_shape.setHeight(Constants.DOODLE_HEIGHT);
		_shape.setX(Constants.DOODLE_X);
		_shape.setY(Constants.DOODLE_Y);
		_shape.setFill(Color.BLACK);
	}
	
	/**
	 * Returns whichever value has been stored as the Doodle's velocity.
	 */
	public double getCurVel() {
		return _velocity;
	}

	/**
	 * Sets the velocity to whichever double value is passed in.
	 */
	public void setCurVel(double vel) {
		_velocity = vel;
	}

	/**
	 * Gets the current y-coordinate of the Doodle's shape.
	 */
	public double getCurY() {
		return _shape.getY();
	}

	/**
	 * Sets the current y-coordinate of the Doodle's shape to whichever double
	 * value is passed in.
	 */
	public void setCurY(double y) {
		_shape.setY(y);
	}

	/**
	 * Gets the current x-coordinate of the Doodle's shape.
	 */
	public double getCurX() {
		return _shape.getX();
	}

	/**
	 * Sets the current x-coordinate of the Doodle's shape to whichever double
	 * value is passed in.
	 */
	public void setCurX(double x) {
		_shape.setX(x);
	}

	/**
	 * Returns a true value if the Doodle's shape intersects with another
	 * shape with passed in dimensional and positional double values.
	 */
	public boolean intersects(double x, double y, double w, double h) {
		return _shape.intersects(x, y, w, h);
	}
}
