/**
 * 
 */
package game.elements;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.material.Material;

/**
 * This class make the little "Sapo", that must be located on top the right hole.
 * The material, of corse, is iron.
 * @author Hari
 *
 */
public class SapoLittle
{
	
	private StaticPhysicsNode sapoLittleStaticNode = null;
	


	// variables
	private Float width = 0.7f;
	private Float height = 0.4f;


	private Float thick = 0.025f;
	
	
	public SapoLittle(PhysicsSpace theSpace, Node rootNode, Vector3f origin)
	{
		sapoLittleStaticNode = theSpace.createStaticNode();
		rootNode.attachChild(sapoLittleStaticNode);
		
//		//Little Sapo back Wall. 
		final Box ls_backWall = new Box("ls_backWall", new Vector3f(), width, height, thick);
		sapoLittleStaticNode.attachChild(ls_backWall);
		ls_backWall.getLocalTranslation().set(new Vector3f(0, 0, -width));

		// The roof
		final Box ls_roof = new Box("ls_roof", new Vector3f(), width, thick, width);
		sapoLittleStaticNode.attachChild(ls_roof);
		ls_roof.getLocalTranslation().set(new Vector3f(0, height, 0));
		
		// The right wall
		final Box rightWall = new Box("rightWall", new Vector3f(), thick, height, width);
		sapoLittleStaticNode.attachChild(rightWall);
		rightWall.getLocalTranslation().set(new Vector3f(width, 0, 0));
		
		// The left wall
		final Box leftWall = new Box("leftWall", new Vector3f(), thick, height, width);
		sapoLittleStaticNode.attachChild(leftWall);
		leftWall.getLocalTranslation().set(new Vector3f(-width, 0, 0));
		
		sapoLittleStaticNode.setMaterial(Material.IRON);
		
		sapoLittleStaticNode.generatePhysicsGeometry();
		sapoLittleStaticNode.getLocalTranslation().set(origin);
	}

	
	public StaticPhysicsNode getSapoLittleStaticNode()
	{
		return sapoLittleStaticNode;
	}
	
	public Float getWidth()
	{
		return width;
	}
	
	public Float getHeight()
	{
		return height;
	}
}
