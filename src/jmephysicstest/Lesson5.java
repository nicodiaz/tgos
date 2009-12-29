package jmephysicstest;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.PhysicsUpdateCallback;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.geometry.PhysicsBox;
import com.jmex.physics.util.SimplePhysicsGame;

public class Lesson5 extends SimplePhysicsGame
{
	// Es global para que podamos leerla en la clase que implementada en el
	// teclado
	private DynamicPhysicsNode dynamicNode;
	private InputHandler physicsStepInputHandler;

	public static void main(String[] args)
	{
		Lesson5 app = new Lesson5();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		Logger.getLogger("").setLevel(Level.WARNING);
		app.start();
	}

	@Override
	protected void simpleInitGame()
	{

		/*
		 * Como de Costumbre, creamos un piso y una esfera suspendida por encima
		 */
		StaticPhysicsNode staticNode = getPhysicsSpace().createStaticNode();
		rootNode.attachChild(staticNode);
		PhysicsBox floorBox = staticNode.createBox("floor");
		floorBox.getLocalScale().set(10, 0.5f, 10);

		dynamicNode = getPhysicsSpace().createDynamicNode();
		rootNode.attachChild(dynamicNode);
		dynamicNode.createSphere("rolling sphere");
		dynamicNode.getLocalTranslation().set(0, 5, 0);

		/*
		 * Necesitamos trabajar con el Handler del Input, y que vaya
		 * actualizandolo a medida que se siga invocando. Poner en "true" el
		 * ultimo parametro de la funcion de Input, de esta forma, hacemos que
		 * el input aplicado sea continuo
		 */
		physicsStepInputHandler = new InputHandler();
		getPhysicsSpace().addToUpdateCallbacks(new PhysicsUpdateCallback() {
			public void beforeStep(PhysicsSpace space, float time)
			{
				physicsStepInputHandler.update(time);
			}

			public void afterStep(PhysicsSpace space, float time)
			{

			}
		});

		// Y definimos las acciones...
		physicsStepInputHandler.addAction(new MyInputAction(new Vector3f(70, 0, 0)),
			InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_HOME, InputHandler.AXIS_NONE, true);

		/*
		 * OJO: Diferencia con el anterior. Aca, el input handler es actualizado
		 * en cada step de actualizacion. Por lo tanto el step de la fuerza es
		 * independiente y tenemos que utilizar este aproach para poder trabajar
		 * correctamente!!! No podemos usar el handler utilizado en la leccion
		 * anterior.
		 */
		// register an action for the other direction, too
		physicsStepInputHandler.addAction(new MyInputAction(new Vector3f(-70, 0, 0)),
			InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_END, InputHandler.AXIS_NONE, true);

		// again we have created only physics - activate physics debug mode to
		// see something
		showPhysics = true;
	}

	private class MyInputAction extends InputAction
	{
		/*
		 * La direccion de la fuerza aplicada, segun la tecla oprimida
		 */
		private final Vector3f direction;

		/*
		 * La fuerza "resultante" aplicada en el instante que se oprime la
		 * tecla. Al momento inicia es nula.
		 */
		private final Vector3f appliedForce = new Vector3f();

		public MyInputAction(Vector3f direction)
		{
			this.direction = direction;
		}

		@Override
		public void performAction(InputActionEvent evt)
		{
			// Seteamos la direccion establecida en el constructor...
			appliedForce.set(direction).multLocal(evt.getTime());
			// ... y finalmente aplicamos la fuerza
			dynamicNode.addForce(appliedForce);
		}

	}

	/*
	 * Como la bola se puede caer del piso, es deseable que apazca de nuevo por
	 * si se cae!! Para ello, en cada atualizacion del frame, vemos si se cayó.
	 * Y en ese caso la restablecemos arriba del piso. (non-Javadoc)
	 * 
	 * @see com.jme.app.BaseSimpleGame#simpleUpdate()
	 */
	protected void simpleUpdate()
	{
		// Ponemos como límite el valor -20 en el eje coordenado y
		if (dynamicNode.getWorldTranslation().y < -20)
		{
			// Borramos velocidad y fuerzar aplicadas
			dynamicNode.clearDynamics();
			// Y la ubicamos arriba del piso de nuevo!!
			dynamicNode.getLocalTranslation().set(0, 5, 0);
		}
	}

}
