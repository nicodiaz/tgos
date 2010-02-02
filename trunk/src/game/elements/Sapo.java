package game.elements;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;

/**
 * This is the modelling of the Sapo Box, with the inner fallings and outter walls.
 * 
 * @author Hari
 * 
 */
public class Sapo
{
	private StaticPhysicsNode sapoStaticNode = null;
	private PhysicsSpace space = null;
	private Node rootNode = null;

	// Wall variables
	private Float sapoWidth = 10.0f;
	private Float sapoHeight = sapoWidth;
	private Float sapoThick = 0.5f;
	private Float sapoBack = 100.0f;
	private Float sapoFront = 80.0f;
	final private Float sapoZMid = (sapoFront + sapoBack) / 2.0f;

	// Inner Falls variables
	// As the width and the heigth are equals, the tg is PI / 4 = 45*
	private Float innerFallsInclination = FastMath.PI / 4.0f;
	private Float innerFallsThick = 0.1f;

	public Sapo(PhysicsSpace theSpace, Node rootNode)
	{
		space = theSpace;
		this.rootNode = rootNode;

		sapoStaticNode = space.createStaticNode();
		rootNode.attachChild(sapoStaticNode);

		makeWalls();
		makeInnerFalls();

		// And finally, generate the require physics
		sapoStaticNode.generatePhysicsGeometry();
	}

	public Sapo(PhysicsSpace theSpace, Node rootNode, Vector3f center)
	{
		this(theSpace, rootNode);
		sapoStaticNode.getLocalTranslation().set(center);

	}

	private void makeWalls()
	{
		// Now we use the boxes of JME, that are trimeshes.

		// We start with the back wall.
		final Box backSapoWall = new Box("backSapoWall", new Vector3f(0, 0,
			-(sapoBack + (sapoThick / 2.0f))), sapoWidth, sapoHeight, sapoThick);
		sapoStaticNode.attachChild(backSapoWall);

		// The floor of the box.
		final Box floorSapoWall = new Box("floorSapoWall", new Vector3f(0,
			-(sapoHeight + sapoThick), -sapoZMid), sapoWidth, sapoThick,
			(sapoBack - sapoFront) / 2.0f);
		sapoStaticNode.attachChild(floorSapoWall);
		
		// TODO:Only for DEBUG. Modify later
		// front wall.
//		final Box frontSapoWall = new Box("frontSapoWall", new Vector3f(0, 0,
//			-sapoFront), sapoWidth, sapoHeight, sapoThick / 10);
//		sapoStaticNode.attachChild(frontSapoWall);
	}

	// TODO: Modify the "heigts" of the falls.
	private void makeInnerFalls()
	{

		// We start with the lower fall.
		final Box lowerFall = new Box("lowerFall", new Vector3f(0, 0, 0), sapoWidth, sapoHeight * 1.4f,
			innerFallsThick);
		lowerFall.getLocalRotation().fromAngleNormalAxis(innerFallsInclination,
			new Vector3f(-1, 0, 0));
		lowerFall.getLocalTranslation().set(new Vector3f(0, 0, -sapoZMid));
		sapoStaticNode.attachChild(lowerFall);

		/*
		 * Now the middle fall. In the variable incY we put the increment on the Y
		 * component (how high is this fall)
		 */
		final Box middleFall = new Box("middleFall", new Vector3f(0, 0, 0), sapoWidth,
			sapoHeight * 1.05f, innerFallsThick);
		middleFall.getLocalRotation().fromAngleNormalAxis(innerFallsInclination,
			new Vector3f(-1, 0, 0));
		Float incY = (sapoZMid - sapoFront) / 4.0f;
		middleFall.getLocalTranslation()
			.set(new Vector3f(0, sapoHeight / 4.0f, -(sapoZMid - incY)));
		sapoStaticNode.attachChild(middleFall);

		// Finally, the higher fall.
		final Box upperFall = new Box("upperFall", new Vector3f(0, 0, 0), sapoWidth,
			sapoHeight * 0.7f, innerFallsThick);
		upperFall.getLocalRotation().fromAngleNormalAxis(innerFallsInclination,
			new Vector3f(-1, 0, 0));
		incY = (sapoZMid - sapoFront) / 2.0f;
		upperFall.getLocalTranslation()
		.set(new Vector3f(0, sapoHeight / 2.0f, -(sapoZMid - incY)));
		sapoStaticNode.attachChild(upperFall);
	}
}
