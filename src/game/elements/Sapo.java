package game.elements;

import game.core.ModelLoader;

import java.util.List;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;

public class Sapo
{
	private StaticPhysicsNode sapoStaticNode = null;

	public Sapo(PhysicsSpace theSpace, Node rootNode)
	{
		sapoStaticNode = theSpace.createStaticNode();
		rootNode.attachChild(sapoStaticNode);

		/*
		 * At first, we set the top of the box of the Sapo
		 */
		Node sapoTopNode = null;
		sapoTopNode = (Node) ModelLoader.load3ds("models/test6.3ds");
		
		sapoStaticNode.getLocalTranslation().set(new Vector3f(-8f, -100f, -300f));
		sapoStaticNode.getLocalScale().set(0.04f, 0.04f, 0.04f);
		sapoStaticNode.getLocalRotation().fromAngleNormalAxis(-(float) Math.PI / 2.0f,
			new Vector3f(1, 0, 0));
		List<Spatial> spatialList = ((Node) sapoTopNode.getChild(0)).getChildren();
		sapoStaticNode.attachChild(spatialList.get(0));

		// The boxes require for the points
//		makeSapoBoxes();
		
		// The lateral walls
//		makeSapoWalls();
		
		
		// Set the properties of the bounding volume, require for the update
//		rootNode.setModelBound(new BoundingBox());
//		rootNode.updateModelBound();

		// And finally, generate the require physics
		sapoStaticNode.generatePhysicsGeometry();
	}

	public Sapo(PhysicsSpace theSpace, Node rootNode, Vector3f center)
	{
		this(theSpace, rootNode);
		sapoStaticNode.getLocalTranslation().set(center);

	}
}
