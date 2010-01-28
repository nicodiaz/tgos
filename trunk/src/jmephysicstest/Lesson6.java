package jmephysicstest;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.contact.ContactInfo;
import com.jmex.physics.geometry.PhysicsBox;

public class Lesson6 extends Lesson5
{
	private StaticPhysicsNode lowerFloor;

	public static void main(String[] args)
	{
		Lesson6 app = new Lesson6();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		Logger.getLogger("").setLevel(Level.WARNING);
		app.start();
	}

	@Override
	protected void simpleInitGame()
	{
		/*
		 * Heredamos todo lo hecho hasta el momento en la lesson 5. Eso incluye el piso,
		 * la esfera suspendida y los inputhandlers de las teclas HOME y END.
		 */
		super.simpleInitGame();

		lowerFloor = getPhysicsSpace().createStaticNode();
		rootNode.attachChild(lowerFloor);
		PhysicsBox floorBox = lowerFloor.createBox("floor");
		floorBox.getLocalScale().set(50.0f, 0.5f, 50.0f);
		lowerFloor.getLocalTranslation().set(0, -10, 0);

		// Creamos otra esfera para testear
		DynamicPhysicsNode sphereNode = getPhysicsSpace().createDynamicNode();
		rootNode.attachChild(sphereNode);
		sphereNode.createSphere("rolling sphere");
		sphereNode.getLocalTranslation().set(5, 5, 0);

		/*
		 * La idea ahora es tomar el handler cuando un objeto colisiona con el piso. La
		 * idea es aplicar la accion que queramos cada vez que se produce una colision con
		 * el piso inferior.
		 */
		final SyntheticButton collisionEventHandler = lowerFloor.getCollisionEventHandler();

		input.addAction(new MyCollisionAction(), collisionEventHandler, false);

	}

	private class MyCollisionAction extends InputAction
	{

		@Override
		public void performAction(InputActionEvent evt)
		{

			/*
			 * Mediante esta forma obtenemos toda la información de la colisión, como por
			 * ejemplo, los datos del objeto que colisionó.
			 */
			final ContactInfo contactInfo = ((ContactInfo) evt.getTriggerData());

			/*
			 * sin embargo, el orden de la colision no esta determinado!!! Entonces puede
			 * ser, en nodo 1 el piso y en nodo 2 la esfera o al reves. Para eso, tenemos
			 * que validarlo de la siguiente manera:
			 */
			DynamicPhysicsNode sphere = null;
			if (contactInfo.getNode2() instanceof DynamicPhysicsNode)
			{
				/*
				 * Bien, el nodo 2 es la esfera. Entonces, la seteamos
				 */
				sphere = (DynamicPhysicsNode) contactInfo.getNode2();
			}
			else if (contactInfo.getNode1() instanceof DynamicPhysicsNode)
			{
				// Es al revés!!
				sphere = (DynamicPhysicsNode) contactInfo.getNode1();
			}

			// Devolvemos el objeto que impacto al punto (0,5,0)
			sphere.clearDynamics();
			sphere.getLocalTranslation().set(0, 5, 0);
		}

	}

}
