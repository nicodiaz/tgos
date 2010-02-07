package game.elements;

import com.jme.bounding.BoundingBox;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.material.Material;


/**
 * This factory creates rooms for the game. However, the idea is to have only one room for
 * the TGOS game.
 * 
 * @author Hari
 *
 */
public class Room extends SapoElement
{
	private StaticPhysicsNode room = null;
	
	// this factor is the size of bigger for lateral
	private Float size = 40.0f;
	
	// doesn't care, how deeper is the floor/wall
	private Float thick = 1.5f;
	
	// How far is the front wall
	private Float far = 120.0f;
	
	// Lights
	private Float lightHigh = size - 4.0f;
	private Float lightFar = far - 20.0f;
	
	
	private float distanceFromFloor = 27.0f;
	
	
	public Room(PhysicsSpace theSpace, Node rootNode, DisplaySystem disp)
	{
		super(theSpace, rootNode, disp);
		
		room = theSpace.createStaticNode();
		room.setModelBound(new BoundingBox());
		room.updateModelBound();
		rootNode.attachChild(room);
		
//		//Now we use the boxes of JME, that are trimeshes. We start with the front Wall. 
		final Box frontWallBox = new Box("frontWall", new Vector3f(), size, size, thick);
		room.attachChild(frontWallBox);
		frontWallBox.getLocalTranslation().set(new Vector3f(0, distanceFromFloor, -far));
//		color(frontWallBox, new ColorRGBA(102.0f / 255f, 51.0f / 255.0f , 255.0f / 255.0f, 1.0f));
		applyTextures(frontWallBox, "models/itbaLogo.jpg");
		
		
		final Box backWallBox = new Box("backWallBox", new Vector3f(), size, size, thick);
		room.attachChild(backWallBox);
		backWallBox.getLocalTranslation().set(new Vector3f(0, distanceFromFloor, far));
//		color(backWallBox, new ColorRGBA(102.0f / 255f, 51.0f / 255.0f , 255.0f / 255.0f, 1.0f));
		applyTextures(backWallBox, "models/itbaLogo.jpg");
		

//		// The floor
		final Box floorBox = new Box("floor", new Vector3f(), size, thick, far);
		room.attachChild(floorBox);
		floorBox.getLocalTranslation().set(new Vector3f(0, -size + distanceFromFloor, 0));
//		color(floorBox, new ColorRGBA(102.0f / 255f, 51.0f / 255.0f , 0.0f, 1.0f));
		applyTextures(floorBox, "models/concreteTexture.jpg");
		

//		// The roof
		final Box roofBox = new Box("roof", new Vector3f(), size, thick, far);
		room.attachChild(roofBox);
		roofBox.getLocalTranslation().set(new Vector3f(0, size + distanceFromFloor, 0));
//		color(roofBox, new ColorRGBA(102.0f / 255f, 51.0f / 255.0f , 0.0f, 1.0f));
		applyTextures(roofBox, "models/roofTexture.jpg");
		
//		// The right wall
		final Box rightWallBox = new Box("rightWall", new Vector3f(), thick, size, far);
		room.attachChild(rightWallBox);
		rightWallBox.getLocalTranslation().set(new Vector3f(size, distanceFromFloor, 0));
//		color(rightWallBox, new ColorRGBA(102.0f / 255f, 51.0f / 255.0f , 255.0f / 255.0f, 1.0f));
		applyTextures(rightWallBox, "models/wallTexture.jpg");
		
//		// The left wall
		final Box leftWallBox = new Box("leftWall", new Vector3f(), thick, size, far);
		room.attachChild(leftWallBox);
		leftWallBox.getLocalTranslation().set(new Vector3f(-size, distanceFromFloor, 0));
//		color(leftWallBox, new ColorRGBA(102.0f / 255f, 51.0f / 255.0f , 255.0f / 255.0f, 1.0f));
		applyTextures(leftWallBox, "models/wallTexture.jpg");
		
		room.setModelBound(new BoundingBox());
		room.updateModelBound();
		
		room.setMaterial(Material.CONCRETE);
		room.generatePhysicsGeometry();
		
		turnOnTheLights();
	}
	
	public Room(PhysicsSpace theSpace, Node rootNode, DisplaySystem disp, float size, float thick, float far)
	{
		this(theSpace, rootNode, disp);
		
		this.size = size;
		this.thick = thick;
		this.far = far;
	}
	
	public StaticPhysicsNode getRoom()
	{
		return room;
	}
	
	private void turnOnTheLights()
	{
		// Set up a basic Point light, that illuminates in all direction.
		PointLight lightFront = new PointLight();
		lightFront.setDiffuse(new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f));
		lightFront.setLocation(new Vector3f(0, lightHigh, -lightFar));
		lightFront.setEnabled(true);

		PointLight lightCenter = new PointLight();
		lightCenter.setDiffuse(new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f));
		lightCenter.setLocation(new Vector3f(0, lightHigh, 10));
		lightCenter.setEnabled(true);
		
		PointLight lightBack = new PointLight();
		lightBack.setDiffuse(new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f));
		lightCenter.setLocation(new Vector3f(0, lightHigh, lightFar));
		lightBack.setEnabled(true);
		
		/** Attach the light to a lightState and the lightState to rootNode. */
		LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
		lightState.attach(lightFront);
//		lightState.attach(lightCenter);
		lightState.attach(lightBack);
		lightState.setTwoSidedLighting(true);
		lightState.setEnabled(true);
		rootNode.setRenderState(lightState);
	}
}
