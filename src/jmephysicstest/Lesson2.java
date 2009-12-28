package jmephysicstest;

import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.util.SimplePhysicsGame;

public class Lesson2 extends SimplePhysicsGame
{

	public static void main(String[] args)
	{
		Lesson2 app = new Lesson2();
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
		 * "hoja" fisica. En este caso utilizamos el Box de JME, ya que tiene
		 * texturas. Además, está fabricado con trimeshes, y vamos a utilizar
		 * como otro mesh.
		 */
		final Box visualFloorBox = new Box("floor", new Vector3f(), 5, 0.25f, 5);
		staticNode.attachChild(visualFloorBox);

		/*
		 * Debemos generar la fisica. Como la primitiva que usamos antes es de
		 * JME, y no de Physics (como el PhysicBox), tenemos que usar una
		 * funcion que la genera. Esto es buenisimo!! O sea, cualquier trimesh,
		 * que no es del physic, te genera la física. Si se le pasa como
		 * parámetro "true", hace las colisiones con cada triangulo del mesh
		 * (pero es mucho mas costoso computacionalmente!!).
		 */
		staticNode.generatePhysicsGeometry();

		/*
		 * Armamos la caja que va a caer, junto con el nodo dinámico. Como es un
		 * Box, tambien tenemos que crear la geometría correspondiente.
		 */
		DynamicPhysicsNode dynamicNode = getPhysicsSpace().createDynamicNode();
		rootNode.attachChild(dynamicNode);
		final Box visualFallingBox = new Box("falling box", new Vector3f(), 0.5f, 0.5f, 0.5f);
		dynamicNode.attachChild(visualFallingBox);
		dynamicNode.generatePhysicsGeometry();
		dynamicNode.getLocalTranslation().set( 0, 5, 0 );

		
		
	}

}
