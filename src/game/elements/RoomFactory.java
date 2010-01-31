package game.elements;

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
public class RoomFactory
{
	private StaticPhysicsNode room = null;
	
	// this factor is the size of bigger for lateral
	private Float SIZEFACTOR = 20.0f;
	
	// doesn't care, how deeper is the floor/wall
	private Float THICK = 1.5f;
	
	// How far is the front wall
	private Float FAR = 80.0f;
	
	
	public RoomFactory(PhysicsSpace theSpace, Node rootNode)
	{
		this(theSpace, rootNode, 20.0f, 1.5f, 80.0f);
	}
	
	public RoomFactory(PhysicsSpace theSpace, Node rootNode, float size, float thick, float far)
	{
		room = theSpace.createStaticNode();
		rootNode.attachChild(room);
		
//		//Now we use the boxes of JME, that are trimeshes. We start with the front Wall. 
//		final Box frontWallBox = new Box("frontWall", new Vector3f(0, 0, -FAR), SIZEFACTOR, SIZEFACTOR, THICK);
//		room.attachChild(frontWallBox);
//
//		// The floor
//		final Box floorBox = new Box("floor", new Vector3f(0, -SIZEFACTOR, 0), SIZEFACTOR, THICK, FAR);
//		room.attachChild(floorBox);
//
//		// The roof
//		final Box roofBox = new Box("roof", new Vector3f(0, SIZEFACTOR, 0), SIZEFACTOR, THICK, FAR);
//		room.attachChild(roofBox);
//		
//		// The right wall
//		final Box rightWallBox = new Box("rightWall", new Vector3f(SIZEFACTOR, 0, 0), THICK, SIZEFACTOR, FAR);
//		room.attachChild(rightWallBox);
//		
//		// The left wall
//		final Box leftWallBox = new Box("leftWall", new Vector3f(-SIZEFACTOR, 0, 0), THICK, SIZEFACTOR, FAR);
//		room.attachChild(leftWallBox);
		
		this.SIZEFACTOR = size;
		this.THICK = thick;
		this.FAR = far;
		
		final Box roomBox = new Box("room", new Vector3f(0, 0, 0), SIZEFACTOR, SIZEFACTOR, FAR);
		room.attachChild(roomBox);
		
		room.generatePhysicsGeometry();
	}
	
	public StaticPhysicsNode getRoom()
	{
		return room;
	}
}
