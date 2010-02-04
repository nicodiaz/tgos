package game.elements;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;


/**
 * This factory creates rooms for the game. However, the idea is to have only one room for
 * the TGOS game.
 * 
 * @author Hari
 *
 */
public class Room
{
	private StaticPhysicsNode room = null;
	
	// this factor is the size of bigger for lateral
	private Float SIZEFACTOR = 40.0f;
	
	// doesn't care, how deeper is the floor/wall
	private Float THICK = 1.5f;
	
	// How far is the front wall
	private Float FAR = 200.0f;
	
	
	public Room(PhysicsSpace theSpace, Node rootNode)
	{
		room = theSpace.createStaticNode();
		room.setModelBound(new BoundingBox());
		room.updateModelBound();
		rootNode.attachChild(room);
		
//		//Now we use the boxes of JME, that are trimeshes. We start with the front Wall. 
		final Box frontWallBox = new Box("frontWall", new Vector3f(), SIZEFACTOR, SIZEFACTOR, THICK);
		room.attachChild(frontWallBox);
		frontWallBox.getLocalTranslation().set(new Vector3f(0, 0, -FAR));
//
//		// The floor
		final Box floorBox = new Box("floor", new Vector3f(), SIZEFACTOR, THICK, FAR);
		room.attachChild(floorBox);
		floorBox.getLocalTranslation().set(new Vector3f(0, -SIZEFACTOR, 0));
//
//		// The roof
		final Box roofBox = new Box("roof", new Vector3f(), SIZEFACTOR, THICK, FAR);
		room.attachChild(roofBox);
		roofBox.getLocalTranslation().set(new Vector3f(0, SIZEFACTOR, 0));
//		
//		// The right wall
		final Box rightWallBox = new Box("rightWall", new Vector3f(), THICK, SIZEFACTOR, FAR);
		room.attachChild(rightWallBox);
		rightWallBox.getLocalTranslation().set(new Vector3f(SIZEFACTOR, 0, 0));
//		
//		// The left wall
		final Box leftWallBox = new Box("leftWall", new Vector3f(), THICK, SIZEFACTOR, FAR);
		room.attachChild(leftWallBox);
		leftWallBox.getLocalTranslation().set(new Vector3f(-SIZEFACTOR, 0, 0));
		
		room.generatePhysicsGeometry();
		
	}
	
	public Room(PhysicsSpace theSpace, Node rootNode, float size, float thick, float far)
	{
		this(theSpace, rootNode);
		
		this.SIZEFACTOR = size;
		this.THICK = thick;
		this.FAR = far;
	}
	
	public StaticPhysicsNode getRoom()
	{
		return room;
	}
}
