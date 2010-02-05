package game.core;

import game.elements.Coin;
import game.elements.CoinFactory;
import game.elements.Room;
import game.elements.Sapo;

import java.util.Date;
import java.util.logging.Logger;

import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.AxisRods;
import com.jme.system.DisplaySystem;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.util.SimplePhysicsGame;

public class TgosMain extends SimplePhysicsGame
{

	// actions variables
	private boolean playerIsThrowing = false;
	private final Float MAXPOWER = 2000.0f;
	private final Float MINPOWER = 100.0f;
	private final Float MULTPOWER = 3f;

	@Override
	protected void simpleInitGame()
	{
		// First, for debug, we set the axis.
		showAxisRods();

		// We start creating the room
		Room room = new Room(getPhysicsSpace(), rootNode, display);

		Sapo sapo = new Sapo(getPhysicsSpace(), rootNode, display);
		
		Coin coin = new Coin(getPhysicsSpace(), rootNode, display);
		
		initActions(coin);
	}

	private void initActions(Coin coin)
	{

		// The throwed coin action.
		final int LEFTMOUSEBUTTON = 0;
		input.addAction(new ThrowCoinAction(coin),
			InputHandler.DEVICE_MOUSE, LEFTMOUSEBUTTON, InputHandler.AXIS_NONE, false);

	}

	private void showAxisRods()
	{
		// Create an right handed axisrods object with a scale of 1/2
		AxisRods ar = new AxisRods("rods", true, 0.5f);
		rootNode.attachChild(ar);
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

				// Create the shoot!!!
				theCoin.setCoinShoot(shootOrigin, shootForce);

				// DEBUG
				System.out.println("POWA: " + power);
				System.out.println("La direccion es: " + cam.getDirection());
				System.out.println("Potencia Aplicada: " + power);
				System.out.println("La fuerza aplicada es es: " + shootForce
					+ " y posición inicial: " + shootOrigin);

				// TODO: Angle
				// Vector3f shootingVector = new Vector3f(power
				// * (float) Math.sin(fireOrientationAngle), 0, power
				// * (float) Math.cos(fireOrientationAngle));
				// System.out.println("shootingVector: " + shootingVector);
				//
				// ((DynamicPhysicsNode) getNode()).addForce(shootingVector);

				// Thw throw has finish
				playerIsThrowing = false;
				/*
				 * System.out.println("Turno antes de disparar: " + turn); turn++;
				 * System.out.println("Turno despues de disparar: " + turn);
				 */
			}
		}
	}
}
