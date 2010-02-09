package game.elements;

import game.utils.ModelLoader;
import game.utils.SapoConfig;

import java.util.List;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.contact.MutableContactInfo;
import com.jmex.physics.material.Material;

// TODO: Change the 3ds model for a jme primitive (cylinder)
//TODO: Make a AbstractCoin class where the PhysicNode exists, maybe solve the black screen problem
public class Coin extends SapoElement
{
	private DynamicPhysicsNode coinDynamicNode = null;
	private Spatial physicCoin = null;
	
	/*
	 * Coin Scene Variables
	 */
	private Float coinScale = 0.0025f;

	/**
	 * The coin to be throw. By default, it's located in the (0, 0, 0)
	 * 
	 * @param theSpace
	 * @param rootNode
	 */
	public Coin(PhysicsSpace theSpace, Node rootNode, DisplaySystem disp)
	{
		super(theSpace, rootNode, disp);

		coinDynamicNode = space.createDynamicNode();
		rootNode.attachChild(coinDynamicNode);
		
		// Recover the scene data from the XML file
		setupSceneData();

		Node coinPhysicNode = (Node) ModelLoader.load3ds("models/coin.3ds");
		List<Spatial> spatialList = ((Node) coinPhysicNode.getChild(0)).getChildren();
		physicCoin = spatialList.get(0);
		coinDynamicNode.attachChild(physicCoin);

		coinDynamicNode.getLocalTranslation().set(new Vector3f(1000f, 1000f, 1000f));
		coinDynamicNode.getLocalScale().set(coinScale, coinScale, coinScale);

		// The coin material must be some sort of metal.
		coinDynamicNode.setMaterial(Material.IRON);
		
//		final Material customMaterial = new Material("metalCoin");
//		customMaterial.setDensity(7.8f);
//		MutableContactInfo contactDetails = new MutableContactInfo();
//		contactDetails.setBounce(0.1f);
//		contactDetails.setMu(0.01f);
//		customMaterial.putContactHandlingDetails(Material.DEFAULT, contactDetails);
//		coinDynamicNode.setMaterial(customMaterial);
		

		coinDynamicNode.setModelBound(new BoundingBox());
		coinDynamicNode.updateModelBound();

		// And finally, generate the require physics
		coinDynamicNode.generatePhysicsGeometry();
		coinDynamicNode.computeMass();
	}

	public Coin(PhysicsSpace theSpace, Node rootNode, DisplaySystem disp, Vector3f origin)
	{
		this(theSpace, rootNode, disp);
		coinDynamicNode.getLocalTranslation().set(origin);
	}

	public Coin(PhysicsSpace theSpace, Node rootNode, DisplaySystem disp, Vector3f origin,
		Vector3f force)
	{
		this(theSpace, rootNode, disp);
		coinDynamicNode.getLocalTranslation().set(origin);
		coinDynamicNode.addForce(force);
	}
	
	private void setupSceneData()
	{
		SapoConfig config = SapoConfig.getInstance();

		coinScale = config.getCoinScale();
	}
	

	public void setCoinShoot(Vector3f origin, Vector3f force)
	{
		coinDynamicNode.getLocalTranslation().set(origin);
		coinDynamicNode.clearDynamics();
		coinDynamicNode.addForce(force);
	}
	
	public Vector3f getLocation()
	{
		return coinDynamicNode.getLocalTranslation();
	}
	
	public Vector3f getVelocity(Vector3f storeVector)
	{
		return coinDynamicNode.getLinearVelocity(storeVector);
	}

	/**
	 * Return the Spatial Node of the coin
	 * 
	 * @return the physicCoin
	 */
	public Spatial getPhysicCoin()
	{
		return physicCoin;
	}
	
}
