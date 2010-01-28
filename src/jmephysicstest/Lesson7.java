package jmephysicstest;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.Joint;
import com.jmex.physics.RotationalJointAxis;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.TranslationalJointAxis;
import com.jmex.physics.geometry.PhysicsBox;
import com.jmex.physics.material.Material;
import com.jmex.physics.util.SimplePhysicsGame;

/**
 * Learn about joints. This application shows a simple example with two joints: a swinging
 * sphere tied to the environment and two boxes tied together.
 * 
 * Un Joint (junta) se trata de dos objetos, vinculados, como atados con una cuerda.
 * 
 * @author Irrisor
 */
public class Lesson7 extends SimplePhysicsGame
{

	private DynamicPhysicsNode dynamicBoxNode1 = null;
	private DynamicPhysicsNode dynamicBoxNode2 = null;

	protected void simpleInitGame()
	{
		/*
		 * Como de costumbre, creamos un piso y una esfera, trasladada.
		 */
		StaticPhysicsNode staticNode = getPhysicsSpace().createStaticNode();
		rootNode.attachChild(staticNode);
		PhysicsBox floorBox = staticNode.createBox("floor");
		floorBox.getLocalScale().set(10, 0.5f, 10);
		DynamicPhysicsNode dynamicSphereNode = getPhysicsSpace().createDynamicNode();
		rootNode.attachChild(dynamicSphereNode);
		dynamicSphereNode.getLocalTranslation().set(3.4F, 5, 0);
		dynamicSphereNode.createSphere("swinging sphere");

		// La corremos un poco para que impacte.
		dynamicSphereNode.getLocalTranslation().set(3.4f, 5, 0);

		/*
		 * Ahora viene lo nuevo. Vamos a "atar" la esfera al entorno, mediante la
		 * siguiente instruccion
		 */
		final Joint jointForSphere = getPhysicsSpace().createJoint();

		/*
		 * Sin embargo, así como está, la junta no tiene ningún grado de libertad. Como en
		 * este caso queremos hacer algo parecido a un péndulo, debemos agragarle un grado
		 * de libertad rotacional, mediante la siguiente instruccion. Lo apuntamos para la
		 * dirección z.
		 */
		final RotationalJointAxis rotationalAxis = jointForSphere.createRotationalAxis();
		rotationalAxis.setDirection(new Vector3f(0F, 0F, 1F));

		/*
		 * Ahora, lo "atamos" al ambiente. En este caso, al estar agragando solo uno de
		 * los extremos, el otro extremo lo ata "al mundo" o ambiente.
		 */
		jointForSphere.attach(dynamicSphereNode);

		/*
		 * Para modificar el punto de mundo donde esta atado, seteamos el anchor point. Es
		 * decir, seteamos donde esta fisicamente el otro extremo.
		 */
		jointForSphere.setAnchor(new Vector3f(0, 5F, 0));

		/*
		 * Ahora creamos dos cajas, que impacten la esfera-pendulo que creamos
		 * anteriormente. Ojo: Recordar que para que cada objeto, es necesario crear su
		 * dynamicPhysicNode correspondiente.
		 */
		dynamicBoxNode1 = getPhysicsSpace().createDynamicNode();
		dynamicBoxNode2 = getPhysicsSpace().createDynamicNode();
		rootNode.attachChild(dynamicBoxNode1);
		dynamicBoxNode1.createBox("box1");
		rootNode.attachChild(dynamicBoxNode2);
		dynamicBoxNode2.createBox("box2");
		dynamicBoxNode1.getLocalTranslation().set(0, 1, 0);
		dynamicBoxNode2.getLocalTranslation().set(0.7f, 1, 0);

		// Le agregamos masa para que sea más pesado.
		dynamicBoxNode1.setMass(5);

		/*
		 * Ahora vamos a juntar las cajas. Para esto, utilizamos una junta traslacional
		 * (?) a diferencia del caso anterior que utilizamos un rotacional. En este caso,
		 * no se cuenta con un punto fijo, y el objeto rotando en torno a él, sino que se
		 * ajustan los dos puntos.
		 */
		final Joint jointForBoxes = getPhysicsSpace().createJoint();
		final TranslationalJointAxis translationalAxis = jointForBoxes.createTranslationalAxis();
		translationalAxis.setDirection(new Vector3f(1F, 0, 0));
		jointForBoxes.attach(dynamicBoxNode1, dynamicBoxNode2);

		/*
		 * Como tener una junta infinita es algo fuera de lo común, la restringimos.
		 */
		translationalAxis.setPositionMinimum(0);
		translationalAxis.setPositionMaximum(10);

		/*
		 * Para que sea un poco "resbalosa" la caja, la hacemos de hielo
		 */
		dynamicBoxNode2.setMaterial(Material.ICE);
		dynamicBoxNode1.setMaterial(Material.RUBBER);

		/*
		 * Vamos a aplicarle la oportunidad de aplicar algunas fuerzas mediante el teclado
		 */
		input.addAction(new ActionBox(1), InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_HOME,
			InputHandler.AXIS_NONE, true);
		input.addAction(new ActionBox(2), InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_END,
			InputHandler.AXIS_NONE, true);

		/*
		 * Como estamos trabajando en forma de debug, hay que setear la visuaciones.
		 */
		showPhysics = true;

	}

	/**
	 * The main method to allow starting this class as application.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args)
	{
		Logger.getLogger("").setLevel(Level.WARNING); // to see the important
		// stuff
		new Lesson7().start();
	}

	private class ActionBox extends InputAction
	{
		private DynamicPhysicsNode currentNode = null;

		// Recibe como parametro el numero de nodo a afectar
		public ActionBox(int i)
		{
			if (i == 1)
			{
				currentNode = dynamicBoxNode1;
			}
			else
			{
				currentNode = dynamicBoxNode2;
			}
		}

		@Override
		public void performAction(InputActionEvent evt)
		{
			/*
			 * Atencion, que las fuerzas se acumulan con cada step!! Ver la proxima
			 * seccion para evitar esto.
			 */
			currentNode.addForce(new Vector3f(10F, 0, 0));
		}

	}
}

/*
 * $log$
 */

