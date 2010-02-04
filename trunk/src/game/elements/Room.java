package game.elements;

import com.jme.bounding.BoundingBox;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;
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
	private Node rootNode = null;
	private PhysicsSpace physicSpace = null;
	private DisplaySystem display = null;
	
	// this factor is the size of bigger for lateral
	private Float size = 40.0f;
	
	// doesn't care, how deeper is the floor/wall
	private Float thick = 1.5f;
	
	// How far is the front wall
	private Float far = 200.0f;
	
	
	public Room(PhysicsSpace theSpace, Node rootNode, DisplaySystem disp)
	{
		physicSpace = theSpace;
		this.rootNode = rootNode;
		display = disp;
		
		room = theSpace.createStaticNode();
		room.setModelBound(new BoundingBox());
		room.updateModelBound();
		rootNode.attachChild(room);
		
//		//Now we use the boxes of JME, that are trimeshes. We start with the front Wall. 
		final Box frontWallBox = new Box("frontWall", new Vector3f(), size, size, thick);
		room.attachChild(frontWallBox);
		frontWallBox.getLocalTranslation().set(new Vector3f(0, 0, -far));
		color(frontWallBox, new ColorRGBA(102.0f / 255f, 51.0f / 255.0f , 255.0f / 255.0f, 1.0f));

//		// The floor
		final Box floorBox = new Box("floor", new Vector3f(), size, thick, far);
		room.attachChild(floorBox);
		floorBox.getLocalTranslation().set(new Vector3f(0, -size, 0));
		color(floorBox, new ColorRGBA(102.0f / 255f, 51.0f / 255.0f , 0.0f, 1.0f));

//		// The roof
		final Box roofBox = new Box("roof", new Vector3f(), size, thick, far);
		room.attachChild(roofBox);
		roofBox.getLocalTranslation().set(new Vector3f(0, size, 0));
		color(roofBox, new ColorRGBA(102.0f / 255f, 51.0f / 255.0f , 0.0f, 1.0f));
		
//		// The right wall
		final Box rightWallBox = new Box("rightWall", new Vector3f(), thick, size, far);
		room.attachChild(rightWallBox);
		rightWallBox.getLocalTranslation().set(new Vector3f(size, 0, 0));
		color(rightWallBox, new ColorRGBA(102.0f / 255f, 51.0f / 255.0f , 255.0f / 255.0f, 1.0f));
		
//		// The left wall
		final Box leftWallBox = new Box("leftWall", new Vector3f(), thick, size, far);
		room.attachChild(leftWallBox);
		leftWallBox.getLocalTranslation().set(new Vector3f(-size, 0, 0));
		color(leftWallBox, new ColorRGBA(102.0f / 255f, 51.0f / 255.0f , 255.0f / 255.0f, 1.0f));
		
		room.generatePhysicsGeometry();
		
//		color(room, new ColorRGBA(102.0f / 255f, 255.0f / 255.0f , 51.0f / 255.0f, 1.0f));
		turnOnTheLights();
	}
	
	public Room(PhysicsSpace theSpace, Node rootNode, DisplaySystem disp, float size, float thick, float far)
	{
		this(theSpace, rootNode, disp);
		
		this.size = size;
		this.thick = thick;
		this.far = far;
	}
	
	/**
	 * Little helper method to color a spatial.
	 * 
	 * @param spatial
	 *            the spatial to be colored
	 * @param color
	 *            desired color
	 */
	private void color(Spatial spatial, ColorRGBA color)
	{
		final MaterialState materialState = display.getRenderer().createMaterialState();
		materialState.setDiffuse(color);
		if (color.a < 1)
		{
			final BlendState blendState = display.getRenderer().createBlendState();
			blendState.setEnabled(true);
			blendState.setBlendEnabled(true);
			blendState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
			blendState.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
			spatial.setRenderState(blendState);
			spatial.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
		}
		spatial.setRenderState(materialState);
	}
	
	public StaticPhysicsNode getRoom()
	{
		return room;
	}
	
	private void turnOnTheLights()
	{
		// Set up a basic Point light, that illuminates in all direction.
		PointLight light = new PointLight();
		light.setDiffuse(new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f));
//		light.setAmbient(new ColorRGBA(0.5f, 0.0f, 0.0f, 1.0f));
		light.setLocation(new Vector3f());
		light.setEnabled(true);

		/** Attach the light to a lightState and the lightState to rootNode. */
		LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
		lightState.attach(light);
		lightState.setTwoSidedLighting(true);
		lightState.setEnabled(true);
		rootNode.setRenderState(lightState);
	}
	
	
}
