package DoodleJump;

import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * This is the class that will be responsible for dealing with the physics of
 * the application, animation, and handling key inputs. An ArrayList adds and
 * removes Platforms.
 */
public class Game {
	private Pane _pane;
	private Doodle _doodle;
	private Timeline _moving;
	private Platform _firstPlatform;
	private ArrayList<Platform> _platforms;
	private PaneOrganizer _paneOrganizer;
	private KeyHandler _keyHandler;
	
	/**
	 * The constructor of this class that instantiates a pane to contain the
	 * animation and then adds a Doodle to this pane. It associates 
	 * itself with the top-level object PaneOrganizer class in order to set
	 * the animation pane to the center of the PaneOrganizer's border pane and
	 * to update the label farther down. It calls the method to set up the 
	 * Timeline moving the Doodle and Platforms, the method to set up key 
	 * inputs, the method to instantiate the first platform, and the method to
	 * generate random platforms to fill the screen
	 */
	public Game(PaneOrganizer organizer) {
		_pane = new Pane();
		_pane.setPrefWidth(Constants.PANE_WIDTH);
		_pane.setPrefHeight(Constants.PANE_HEIGHT);
		_paneOrganizer = organizer;
		_paneOrganizer.getRoot().setCenter(_pane);
		_doodle = new Doodle(_pane);
		this.setupTimeHandler();
		this.setUpKeyHandler();
		this.setUpPlatform();
		this.generatePlatforms();
	}

	/**
	 * Instantiates the first platform to beneath the Doodle's starting 
	 * position. Adds this platform then both logically and graphically.
	 */
	private void setUpPlatform() {
		_firstPlatform = new Platform(_pane);
		_firstPlatform.setCurX(Constants.PLATFORM_X);
		_firstPlatform.setCurY(Constants.PLATFORM_Y);
		_platforms = new ArrayList<Platform>();
		_platforms.add(_firstPlatform);
	}

	/**
	 * Instantiates a KeyFrame specifying how long the animation should run for
	 * and which event handler to use it with. Also instantiates the Timeline
	 * itself, sets the animation to play indefinitely, and then plays the 
	 * animation.
	 */
	public void setupTimeHandler() {
		KeyFrame moveFrame = new KeyFrame(Duration.seconds(Constants.DURATION), 
				new TimeHandler());

		_moving = new Timeline(moveFrame);

		_moving.setCycleCount(Animation.INDEFINITE);

		_moving.play();
	}

	/**
	 * Class to implement handle() method specifying what should occur at the
	 * end of each KeyFrame for proper physics simulation and movement of the
	 * Doodle when jumping off of platforms and falling as well as generation
	 * and scrolling of platforms. Implements EventHandler interface.
	 */
	private class TimeHandler implements EventHandler<ActionEvent> { 
		private double _distanceAboveMid;
		
		/** 
		 * Empty constructor.
		 */
		public TimeHandler() {
		}

		/**
		 * The velocity of the Doodle is updated with a defined
		 * kinematic equation at the end of each KeyFrame. The position of the 
		 * Doodle is also updated but dependent on whether the Doodle is above
		 * the midpoint of the pane. If it is, the difference between the
		 * midpoint and the Doodle is stored, the Doodle is set to the midpoint,
		 * and the platforms scroll downwards by stored value. Otherwise, the 
		 * Doodle is set to the calculated position using its velocity. The 
		 * methods responsible for colliding the Doodle against the sides of the
		 * pane, bouncing the Doodle off of platforms, and telling the player 
		 * that the game is over are called. 
		 */
		public void handle(ActionEvent event) {
			double updatedVelocity = _doodle.getCurVel() 
					+ Constants.GRAVITY 
					* Constants.DURATION;
			double updatedPosition = _doodle.getCurY() 
					+ updatedVelocity 
					* Constants.DURATION;
			_doodle.setCurVel(updatedVelocity);
			if (updatedPosition < Constants.PANE_HEIGHT / 2) {
				_distanceAboveMid = (Constants.PANE_HEIGHT / 2) 
						- updatedPosition;
				_doodle.setCurY(Constants.PANE_HEIGHT / 2);
				this.scroll();
			} else
				_doodle.setCurY(updatedPosition);
			this.collide();
			this.bounce();
			Game.this.gameOver();
		}

		/**
		 * Platforms are scrolled downward via a for loop that loops through
		 * the ArrayList of platforms and moves them downwards by the stored
		 * distance between the Doodle and the pane's midpoint. Platforms are
		 * than randomly generated above the current platform. Platforms are
		 * removed both graphically and logically once they pass the bottom of
		 * the pane.
		 */
		private void scroll() {
			for (int i = 0; i < _platforms.size(); i++) {
					_platforms.get(i).setCurY(_platforms.get(i).getCurY() 
							+ _distanceAboveMid);
					Game.this.generatePlatforms();
					if (_platforms.get(i).getCurY() 
							+ Constants.PLATFORM_HEIGHT 
							> Constants.PANE_HEIGHT) {
						_platforms.get(i).removePlatform(_pane);
						_platforms.remove(i);
					}
				}
			}

		/**
		 * The Doodle is limited to the edges of the pane via checking its
		 * x-position against the coordinates of the edges.
		 */
		private void collide() {
			if (_doodle.getCurX() < 0) {
				_doodle.setCurX(0);
			}
			if (_doodle.getCurX() 
					> Constants.PANE_WIDTH 
					- Constants.DOODLE_WIDTH) {
				_doodle.setCurX(Constants.PANE_WIDTH 
						- Constants.DOODLE_WIDTH);
			}
		}

		/**
		 * The Doodle bounces off of any given platform via a for loop looping
		 * through the ArrayList of platforms checking if the Doodle intersects
		 * with any of them and if they Doodle is moving downwards as opposed
		 * to upwards. The Doodle's velocity is then set to a larger negative
		 * value, giving it the appearance of jumping.
		 */
		private void bounce() {
			for (int i = 0; i < _platforms.size(); i++) {
				if (_doodle.intersects(_platforms.get(i).getCurX(), 
						_platforms.get(i).getCurY(),
						Constants.PLATFORM_WIDTH, 
						Constants.PLATFORM_HEIGHT) 
						&& _doodle.getCurVel() >= 0) {
					_doodle.setCurVel(Constants.REBOUND_VELOCITY);
				}
			}
		}
	}

	
	/**
	 * Instantiates and adds the KeyHandler to the pane. Sets the focus to the
	 * animation pane so that other nodes do not grab focus.
	 */
	private void setUpKeyHandler() {
		_keyHandler = new KeyHandler();
		_pane.addEventHandler(KeyEvent.KEY_PRESSED, _keyHandler);
		_pane.setFocusTraversable(true);
	}

	/**
	 * Class responsible for key input to move the Doodle via implementing
	 * EventHandler interface.
	 */
	private class KeyHandler implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent e) {

			KeyCode keyPressed = e.getCode();
			switch (keyPressed) {
			case LEFT:
				_doodle.setCurX(_doodle.getCurX() - Constants.MOVE_X);
				break;
			case RIGHT:
				_doodle.setCurX(_doodle.getCurX() + Constants.MOVE_X);
				break;
			default:
				break;

			}
			e.consume(); // only executes code specified above
		}
	}

	/**
	 * Generates new platforms above current platform(s). The most recently
	 * added platform is checked to see if it is below the top of the pane.
	 * Only then is a new Platform graphically added to the pane. Low and high
	 * values are stored to then generate random x and y-coordinates for the
	 * new Platform. The new Platform is then set to these coordinate and added
	 * to the ArrayList.
	 */
	private void generatePlatforms() {
		Platform previousPlatform = _platforms.get(_platforms.size() - 1);
		while (previousPlatform.getCurY() > 0) {
			Platform nextPlatform = new Platform(_pane);
			// To ensure adequate space vertically between platforms
			double lowY = previousPlatform.getCurY() 
					- 4 * Constants.PLATFORM_HEIGHT;
			// To ensure the Doodle can reach the next platform vertically
			double highY = previousPlatform.getCurY() 
					- Constants.DOODLE_Y_JUMP;
			nextPlatform.setCurY(randomInt(lowY, highY));
			// To ensure the Doodle can reach the next platform horizontally
			double lowX = previousPlatform.getCurX() 
					- Constants.DOODLE_X_JUMP;
			double highX = previousPlatform.getCurX() 
					+ Constants.DOODLE_X_JUMP;
			// To ensure new Platform is generated within the pane.
			if (lowX - Constants.PLATFORM_WIDTH <= 0 || highX 
					+ Constants.PLATFORM_WIDTH >= Constants.PANE_WIDTH) {
				lowX = highX = Constants.PLATFORM_X;
			}
			nextPlatform.setCurX(randomInt(lowX, highX));
			/* 
			 * This newly generated Platform is then stored as the most
			 * recently generated Platform
			 */
			previousPlatform = nextPlatform;
			_platforms.add(previousPlatform);
		}
	}

	/** 
	 * The stored low and high values for x and y-coordinates are plugged in
	 * here and interact with a random integer between 0-9 to return a random
	 * double coordinate.
	 */
	private double randomInt(double low, double high) {
		return low + (int) (Math.random() * (high - low + 1));
	}

	/**
	 * Once the Doodle falls past the bottom of the pane, the PaneOrganizer
	 * updates the label and the KeyHandler is disabled to prevent the Doodle
	 * from moving.
	 */
	private void gameOver() {
		if (_doodle.getCurY() > Constants.PANE_HEIGHT) {
			_paneOrganizer.updateLabel();
			_pane.removeEventHandler(KeyEvent.KEY_PRESSED, _keyHandler);
		}
	}
}
