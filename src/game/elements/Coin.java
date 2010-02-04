package game.elements;

import game.core.ModelLoader;

import java.util.List;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.material.Material;


// TODO: Change the 3ds model for a jme primitive (cylinder)
//TODO: Make a AbstractCoin class where the PhysicNode exists, maybe solve the black screen problem
public class Coin
{
	private DynamicPhysicsNode coinDynamicNode = null;
	private Spatial physicCoin = null;

	/**
	 * The coin to be throw. By default, it's located in the (0, 0, 0)
	 * 
	 * @param theSpace
	 * @param rootNode
	 */
	public Coin(PhysicsSpace theSpace, Node rootNode)
	{
		coinDynamicNode = theSpace.createDynamicNode();
		rootNode.attachChild(coinDynamicNode);
		coinDynamicNode.setModelBound(new BoundingBox());
		coinDynamicNode.updateModelBound();

		Node coinPhysicNode = (Node) ModelLoader.load3ds("models/coin.3ds");
//		coinDynamicNode.getLocalRotation().fromAngleNormalAxis(-(float) Math.PI / 2.0f,
//			new Vector3f(1, 0, 0));
		List<Spatial> spatialList = ((Node) coinPhysicNode.getChild(0)).getChildren();
		physicCoin = spatialList.get(0);
		coinDynamicNode.attachChild(physicCoin);
		
		coinDynamicNode.getLocalTranslation().set(new Vector3f(-13f, 50f, -310f));
		coinDynamicNode.getLocalScale().set(0.0025f, 0.0025f, 0.0025f);

		// The coin material must be some sort of metal.
		coinDynamicNode.setMaterial(Material.IRON);
		
		// And finally, generate the require physics
		coinDynamicNode.generatePhysicsGeometry();
		coinDynamicNode.computeMass();
	}

	public Coin(PhysicsSpace theSpace, Node rootNode, Vector3f origin)
	{
		this(theSpace, rootNode);
		coinDynamicNode.getLocalTranslation().set(origin);
	}

	public Coin(PhysicsSpace theSpace, Node rootNode, Vector3f origin, Vector3f force)
	{
		this(theSpace, rootNode);
		coinDynamicNode.getLocalTranslation().set(origin);
		coinDynamicNode.addForce(force);
	}
}
