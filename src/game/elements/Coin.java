package game.elements;

import java.util.List;

import game.core.ModelLoader;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Disk;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.geometry.PhysicsCylinder;
import com.jmex.physics.material.Material;

public class Coin
{
	private DynamicPhysicsNode coinNode = null;
	
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
//		
//		PhysicsCylinder coin = coinNode.createCylinder("coin");
//		coin.setMaterial(Material.IRON);
//		
//		
////		// The coin itself, made by a cylinder
//		coin.getLocalScale().set(20f, 20f, 20f);
////		coin.getLocalTranslation().set(10f, 10f, 10f);
//
//		
////		coinNode.setModelBound(new BoundingBox());
//		coinNode.setModelBound(new BoundingSphere());
//		coinNode.updateModelBound();
////		
//		coinNode.attachChild(coin);
//		coinNode.generatePhysicsGeometry();
//		coinNode.computeMass();
//		
//		// DEBUg
//		coinNode.getLocalTranslation().set(new Vector3f(0f, 0f, 0f));
		
		Node coinPhysicNode = null;
		coinPhysicNode = (Node) ModelLoader.load3ds("models/coin.3ds");
		coinNode.getLocalTranslation().set(new Vector3f(-13f, 50f, -310f));
		coinNode.getLocalScale().set(0.02f, 0.02f, 0.02f);
		coinNode.getLocalRotation().fromAngleNormalAxis(-(float) Math.PI / 2.0f,
			new Vector3f(1, 0, 0));
		List<Spatial> spatialList = ((Node) coinPhysicNode.getChild(0)).getChildren();
		coinNode.attachChild(spatialList.get(0));
		coinNode.setModelBound(new BoundingBox());
		coinNode.updateModelBound();

		// And finally, generate the require physics
		coinNode.generatePhysicsGeometry();
	}
	
	public Coin(PhysicsSpace theSpace, Node rootNode, Vector3f origin)
	{
		coinNode.getLocalTranslation().set(origin);
	}

}
