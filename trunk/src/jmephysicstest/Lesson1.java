package jmephysicstest;

import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.geometry.PhysicsBox;
import com.jmex.physics.util.SimplePhysicsGame;

public class Lesson1 extends SimplePhysicsGame
{

	public static void main(String[] args)
	{
		Lesson1 app = new Lesson1();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	@Override
	protected void simpleInitGame()
	{
		/*
		 * Tenemos que crear un Nodo estatico, que es un nodo que nada lo puede
		 * mover, pero que afecta a la física de los objetos que interactúan con
		 * él. Lo agregamos al rootNode.
		 */
		StaticPhysicsNode staticNode = getPhysicsSpace().createStaticNode();
		rootNode.attachChild(staticNode);

		/*
		 * Ahora que ya tenemos definido el staticNode, tenemos que hacerle una
		 * "hoja" fisica. Una caja es un buen ejemplo. De paso la escalamos.
		 */
		PhysicsBox floorBox = staticNode.createBox("floor");
		floorBox.getLocalScale().set(10, 0.5f, 10);

		/*
		 * Ahora tenemos que crear el objeto físico que se va a mover. Para
		 * ello, en vez de static tenemos qe hacer un dynamic node. Y tambien lo
		 * agregamos al root.
		 */
		DynamicPhysicsNode dynamicNode = getPhysicsSpace().createDynamicNode();
		rootNode.attachChild(dynamicNode);

		/*
		 * ahora tenemos que crear el objeto físico que va a tener movimiento.
		 * Nuevamente, es una hoja del nodo interno dinámico. Tambien lo
		 * escalamos.
		 */
		dynamicNode.createBox("falling box");
		dynamicNode.getLocalTranslation().set(0, 5, 0);

		/*
		 * Activamos esto porque al no haber seteado ningun textura, no veríamos
		 * nada sino.
		 */
		showPhysics = true;

	}

}
