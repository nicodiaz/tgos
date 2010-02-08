package game.core;

import game.elements.Coin;
import game.elements.Room;
import game.elements.Sapo;
import game.utils.CameraOptions;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;
import com.jme.scene.Text;
import com.jme.scene.shape.AxisRods;
import com.jmex.physics.util.SimplePhysicsGame;

public class TgosMain extends SimplePhysicsGame
{
	// Update varibales
	private boolean coinInMovement = false;
	private Date initTime = new Date(); // the time that start the shoot
	private Date lastTime = null;
	private Float initShootTime = 0.0f;
	private Vector3f currentVelocity = new Vector3f();
	private CameraOptions cameraOption = CameraOptions.FreeStyleCamera;
	private boolean deactivateMouseShooting = false;

	// The players data. In the future, this can be a class.
	private Integer[] playerScores = { 0, 0 };
	private Integer[] playerShoots = { 0, 0 };
	private Integer playerTurn = 0;

	// Global Elements
	private Coin coin = null;
	private Sapo sapo = null;

	// The text information.
	private Text playerInfoText = null;
	private Text shootInfoText = new Text();
	private Text scoreInfoText = new Text();
	private Text sapoHitText = null;

	// actions variables
	private boolean playerIsThrowing = false;
	private final Float MAXPOWER = 2000.0f;
	private final Float MINPOWER = 100.0f;
	private final Float MULTPOWER = 3f;

	@Override
	protected void simpleInitGame()
	{
		Logger.getLogger("").log(Level.WARNING, "Cargando Juego. Por favor espere...");
		// First, for debug, we set the axis.
		showAxisRods();

		// We start creating the room
		Room room = new Room(getPhysicsSpace(), rootNode, display);

		sapo = new Sapo(getPhysicsSpace(), rootNode, display);

		coin = new Coin(getPhysicsSpace(), rootNode, display);

		initActions();
		makeTexts();
		Logger.getLogger("").log(Level.WARNING, "Juego cargado. Enjoy!");
	}

	private void makeTexts()
	{
		// The player info text
		playerInfoText = Text.createDefaultTextLabel("playerInfoText", createPlayerInfoText());
		playerInfoText.getLocalTranslation().set(0, display.getHeight() - 20.0f, 0);
		playerInfoText.getLocalScale().set(1.0f, 1.0f, 1.0f);
		playerInfoText.getTextColor().set(1.0f, 0.0f, 0.0f, 1.0f);
		statNode.attachChild(playerInfoText);

		// The score of the player
		scoreInfoText = Text.createDefaultTextLabel("scoreInfoText", createScoreInfoText());
		scoreInfoText.getLocalTranslation().set(0, display.getHeight() - 50.0f, 0);
		scoreInfoText.getLocalScale().set(1.0f, 1.0f, 1.0f);
		scoreInfoText.getTextColor().set(1.0f, 0.0f, 0.0f, 1.0f);
		statNode.attachChild(scoreInfoText);

	}

	private String createPlayerInfoText()
	{
		return "Turno Jugador " + (playerTurn + 1) + ". Tiros hasta el momento: "
			+ playerShoots[playerTurn];
	}

	private String createScoreInfoText()
	{
		return "Puntos hasta el momento: " + playerScores[playerTurn];
	}

	private void createShootInfoText(Integer lastPoints)
	{
		// Text 2d in front of the camera

		shootInfoText = new Text("shootInfoText", lastPoints.toString() + " puntos!!!");
		shootInfoText.getLocalTranslation().set(display.getWidth() / 2.0f - 100.0f,
			display.getHeight() / 2.0f, 0);
		shootInfoText.getTextColor().set(1.0f, 0, 0.0f, 1.0f);
		shootInfoText.getLocalScale().set(2.0f, 2.0f, 2.0f);

		statNode.attachChild(shootInfoText);

		if (lastPoints == 150)
		{
			sapoHitText = new Text("sapoHitText", "Sapo Hit!!!");
			sapoHitText.getLocalTranslation().set(display.getWidth() / 2.0f - 150.0f,
				shootInfoText.getLocalTranslation().y - 50.0f, 0);
			sapoHitText.getTextColor().set(0.0f, 1.0f, 0, 1.0f);
			sapoHitText.getLocalScale().set(3.0f, 3.0f, 3.0f);

			statNode.attachChild(sapoHitText);
		}

	}

	private void initActions()
	{

		// The throwed coin action.
		final int LEFTMOUSEBUTTON = 0;
		input.addAction(new ThrowCoinAction(coin), InputHandler.DEVICE_MOUSE, LEFTMOUSEBUTTON,
			InputHandler.AXIS_NONE, false);

		// The camera choose
		input.addAction(new ChangeCameraToFreeStyleAction(), InputHandler.DEVICE_KEYBOARD,
			KeyInput.KEY_1, InputHandler.AXIS_NONE, false);
		input.addAction(new ChangeCameraToSapoAction(), InputHandler.DEVICE_KEYBOARD,
			KeyInput.KEY_2, InputHandler.AXIS_NONE, false);
		input.addAction(new ChangeCameraToCoinAction(), InputHandler.DEVICE_KEYBOARD,
			KeyInput.KEY_3, InputHandler.AXIS_NONE, false);
		input.addAction(new ResetCameraPositionAction(), InputHandler.DEVICE_KEYBOARD,
			KeyInput.KEY_4, InputHandler.AXIS_NONE, false);

	}

	private void showAxisRods()
	{
		// Create an right handed axisrods object with a scale of 1/2
		AxisRods ar = new AxisRods("rods", true, 0.5f);
		rootNode.attachChild(ar);
	}

	@Override
	protected void simpleUpdate()
	{

		// This if is to check if the coin is quiet or not.
		if (coinInMovement)
		{
			if (!deactivateMouseShooting)
			{
				deactivateMouseShooting = true;
			}
			
			// We must check if the cam is allright
			if (cameraOption == CameraOptions.SapoCamera)
			{
				// Sapo-Cam
				if (!cam.getLocation().equals(sapo.getLittleSapoPosition()))
				{
					cam.getLocation().set(sapo.getLittleSapoPosition());
				}
				cam.lookAt(coin.getLocation(), Vector3f.UNIT_Y);
			}
			else if (cameraOption == CameraOptions.CoinCamera)
			{
				// Moneda-Cam
				cam.getLocation().set(coin.getLocation());
				cam.update();
			}

			lastTime = new Date();
			long timeDiff = lastTime.getTime() - initTime.getTime();

			if (Math.floor(timeDiff / 1000.0) > 0.5)
			{
				initTime = lastTime;
				// A half a second have passed. We must verify the velocities.
				currentVelocity = coin.getVelocity(null);
				System.out.println("DEBUG: Vector Velocidad: " + currentVelocity.x + ", "
					+ currentVelocity.y + ", " + currentVelocity.z);
				System.out.println("DEBUG: Distancia: " + currentVelocity.distance(new Vector3f()));
				if (isCoinStopped())
				{
					coinInMovement = false;

					if (cameraOption != CameraOptions.FreeStyleCamera)
					{
						// We must restore the camera to the start point
						cam.getLocation().set(Vector3f.ZERO);
						cam.lookAt(sapo.getLittleSapoPosition(), Vector3f.UNIT_Y);
					}

					// we must check if is touching a box
					Integer lastPoints = sapo.getPoints(coin);
					System.out.println("DEBUG: HIZO " + sapo.getPoints(coin) + " PUNTOS");
					playerScores[playerTurn] += lastPoints;
					createShootInfoText(lastPoints);
					initShootTime = timer.getTimeInSeconds();
					
					// We cannot forget to activate again the mouse
					deactivateMouseShooting = false;
					
				}
			}
			else
			{
				// Nothing to do, we must wait a delta
			}
		}

		// Now, we must update the player information
		playerInfoText.print(createPlayerInfoText());
		scoreInfoText.print(createScoreInfoText());

		if (timer.getTimeInSeconds() > (initShootTime + 3.0f))
		{
			statNode.detachChild(shootInfoText);

			if (sapoHitText != null)
			{
				statNode.detachChild(sapoHitText);
			}
			cam.update();
		}

	}

	private boolean isCoinStopped()
	{
		if (currentVelocity.distance(Vector3f.ZERO) == 0.0f)
		{
			// This is to avoid a problem with JMEPhysics
			return false;
		}

		return (currentVelocity.distance(Vector3f.ZERO) < 0.1 ? true : false);
	}

	/*
	 * The Action Clases.
	 */
	private class ThrowCoinAction extends InputAction
	{
		private Coin theCoin = null;

		public ThrowCoinAction(Coin theCoin)
		{
			this.theCoin = theCoin;
		}

		private Date initMoment = null;

		/**
		 * This method contain the actions when the throw key is pressed/realeased.
		 * 
		 * @param evt
		 *            Information about the event.
		 */
		@Override
		public void performAction(InputActionEvent evt)
		{
			
			if (deactivateMouseShooting)
			{
				// We cannot do anything
				return ;
			}
			

			if (!playerIsThrowing)
			{
				if (evt.getTriggerPressed())
				{
					/*
					 * The player pressed the key and start a throw!!!. We must take that
					 * moment!
					 */
					initMoment = new Date();
					playerIsThrowing = true;
				}
			}
			else if (playerIsThrowing && !evt.getTriggerPressed())
			{
				// The player has release the button
				Date finishMoment = new Date();

				// We must scale the power to our scale, between min and max power.
				Float power = (finishMoment.getTime() - initMoment.getTime()) * MULTPOWER;

				// Debug
				System.out.println("La resta de tiempos es: "
					+ (finishMoment.getTime() - initMoment.getTime()));

				if (power < MINPOWER)
				{
					power = MINPOWER;
				}
				else if (power > MAXPOWER)
				{
					power = MAXPOWER;
				}
				Vector3f shootOrigin = cam.getLocation();
				Vector3f shootDirection = cam.getDirection();
				Vector3f shootForce = shootDirection.mult(power);

				/*
				 * Create the shoot!!! We must set the shoot itself, the time and the
				 * place where happened.
				 */
				theCoin.setCoinShoot(shootOrigin, shootForce);
				coinInMovement = true;
				initMoment = new Date();
				playerShoots[playerTurn]++;

				// DEBUG
				System.out.println("POWA: " + power);
				System.out.println("La direccion es: " + cam.getDirection());
				System.out.println("Potencia Aplicada: " + power);
				System.out.println("La fuerza aplicada es es: " + shootForce
					+ " y posición inicial: " + shootOrigin);

				// The throw has finish
				playerIsThrowing = false;
				
				// We disable the mouse
				deactivateMouseShooting = true;
			}
		}
	}

	private class ChangeCameraToSapoAction extends InputAction
	{

		@Override
		public void performAction(InputActionEvent evt)
		{
			cameraOption = CameraOptions.SapoCamera;
		}
	}

	private class ChangeCameraToFreeStyleAction extends InputAction
	{

		@Override
		public void performAction(InputActionEvent evt)
		{
			cameraOption = CameraOptions.FreeStyleCamera;
		}
	}

	private class ChangeCameraToCoinAction extends InputAction
	{

		@Override
		public void performAction(InputActionEvent evt)
		{
			cameraOption = CameraOptions.CoinCamera;
		}
	}

	private class ResetCameraPositionAction extends InputAction
	{

		@Override
		public void performAction(InputActionEvent evt)
		{
			// We must restore the camera to the start point
			cam.getLocation().set(Vector3f.ZERO);
			cam.lookAt(sapo.getLittleSapoPosition(), Vector3f.UNIT_Y);
			cam.update();
			cameraOption = CameraOptions.FreeStyleCamera;
		}
	}
}
