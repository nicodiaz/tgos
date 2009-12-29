package jmephysicstest;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.geometry.PhysicsBox;
import com.jmex.physics.util.SimplePhysicsGame;

public class Lesson4 extends SimplePhysicsGame
{
	// Es global para que podamos leerla en la clase que implementada en el
	// teclado
	private DynamicPhysicsNode dynamicNode;

	public static void main(String[] args)
	{
		Lesson4 app = new Lesson4();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		Logger.getLogger("").setLevel(Level.WARNING);
		app.start();
	}

	@Override
	protected void simpleInitGame()
	{
		/*
		 * Creamos un piso de las lecciones 1 y 2. OJO: Ponemos los valores y NO
		 * un new Vecto3f ya que sino lo estaría creando con cata iteracion (?)
		 */
		StaticPhysicsNode staticNode = getPhysicsSpace().createStaticNode();
		rootNode.attachChild(staticNode);
		PhysicsBox floorBox = staticNode.createBox("floor");
		floorBox.getLocalScale().set(10, 0.5f, 10);

		/*
		 * Ahora creamos la esfera que va a impactar con el piso. Para eso, al
		 * ser un objeto fisico dinámico, como siempre, tenemos que crear
		 * primero el dynamic node y luego trasladarlo.
		 */
		dynamicNode = getPhysicsSpace().createDynamicNode();
		rootNode.attachChild(dynamicNode);
		dynamicNode.createSphere("rolling sphere");
		dynamicNode.getLocalTranslation().set(0, 5, 0);

		/*
		 * Otro tip! Modificar alguna asociacion con el teclado. El ultimo false
		 * es para la repeticion de la tecla, que indica que la acción que se va
		 * a ejecutar cada vez que se presiona la tecla.
		 */
		input.addAction(new MyInputAction(), InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_HOME,
			InputHandler.AXIS_NONE, true);

		/*
		 * Empezamos pausado para poder ver bien ;)
		 */
		pause = true;
	}

	/*
	 * Clase privada para realizar la opcion cada vez que se presiona la tecla
	 * correspondiente. Principalmente es un listener para el evento de
	 * presionar la tecla.
	 */
	private class MyInputAction extends InputAction
	{

		@Override
		public void performAction(InputActionEvent evt)
		{
			/*
			 * Atencion, que las fuerzas se acumulan con cada step!! Ver la
			 * proxima seccion para evitar esto.
			 */
			dynamicNode.addForce(new Vector3f(2, 0, 0));
		}

	}

	/*
	 * Como pusimos que la bola se mueva solo en una direccion, es deseable que
	 * apazca de nuevo por si se cae!! Para ello, en cada atualizacion del
	 * frame, vemos si se cayó. Y en ese caso la restablecemos arriba del piso.
	 * (non-Javadoc)
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
