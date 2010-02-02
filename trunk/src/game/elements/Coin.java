package game.elements;

import game.core.ModelLoader;

import java.util.List;

import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.material.Material;


// TODO: Change the 3ds model for a jme primitive (cylinder)
public class Coin
{
	private DynamicPhysicsNode coinNode = null;
	private Spatial physicCoin = null;

	/**
	 * The coin to be throw. By default, it's located in the (0, 0, 0)
	 * 
	 * @param theSpace
	 * @param rootNode
	 */
	public Coin(PhysicsSpace theSpace, Node rootNode)
	{
		coinNode = theSpace.createDynamicNode();
		rootNode.attachChild(coinNode);

		Node coinPhysicNode = (Node) ModelLoader.load3ds("models/coin.3ds");
//		coinNode.getLocalRotation().fromAngleNormalAxis(-(float) Math.PI / 2.0f,
//			new Vector3f(1, 0, 0));
		List<Spatial> spatialList = ((Node) coinPhysicNode.getChild(0)).getChildren();
		physicCoin = spatialList.get(0);
		coinNode.attachChild(physicCoin);
		
		coinNode.getLocalTranslation().set(new Vector3f(-13f, 50f, -310f));
		coinNode.getLocalScale().set(0.0025f, 0.0025f, 0.0025f);
		
		coinNode.setModelBound(new BoundingSphere());
		coinNode.updateModelBound();

		coinNode.setMaterial(Material.ICE);
		
		// And finally, generate the require physics
		coinNode.generatePhysicsGeometry();
		coinNode.computeMass();
		
		final Material coinMaterial = new Material("coin material");
		coinNode.setMaterial(coinMaterial);
		
	}

	public Coin(PhysicsSpace theSpace, Node rootNode, Vector3f origin)
	{
		this(theSpace, rootNode);
		coinNode.getLocalTranslation().set(origin);
	}

	public Coin(PhysicsSpace theSpace, Node rootNode, Vector3f origin, Vector3f force)
	{
		this(theSpace, rootNode);
		coinNode.getLocalTranslation().set(origin);
		coinNode.addForce(force);
	}
}
