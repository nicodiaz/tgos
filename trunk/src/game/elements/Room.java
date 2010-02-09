package game.elements;

import game.utils.SapoConfig;

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
	
	/*
	 * Room Scene Variables
	 */
	// this factor is the roomSize of bigger for lateral
	private Float roomSize = 40.0f;
	// doesn't care, how deeper is the floor/wall
	private Float roomThick = 1.5f;
	// How roomFar is the front wall
	private Float roomFar = 120.0f;
	private Float distanceFromFloor = 27.0f;
	
	
	
	/*
	 * Lights
	 */
	private Float lightHigh = roomSize - 4.0f;
	private Float lightFar = roomFar - 20.0f;
	
	public Room(PhysicsSpace theSpace, Node rootNode, DisplaySystem disp)
	{
		super(theSpace, rootNode, disp);
		
		room = theSpace.createStaticNode();
		room.setModelBound(new BoundingBox());
		room.updateModelBound();
		rootNode.attachChild(room);
		
		// Recover the scene data from the XML file
		setupSceneData();
		
		//Now we use the boxes of JME, that are trimeshes. We start with the front Wall. 
		final Box frontWallBox = new Box("frontWall", new Vector3f(), roomSize, roomSize, roomThick);
		room.attachChild(frontWallBox);
		frontWallBox.getLocalTranslation().set(new Vector3f(0, distanceFromFloor, -roomFar));
		applyTextures(frontWallBox, "models/itbaLogo.jpg");
		
		
		final Box backWallBox = new Box("backWallBox", new Vector3f(), roomSize, roomSize, roomThick);
		room.attachChild(backWallBox);
		backWallBox.getLocalTranslation().set(new Vector3f(0, distanceFromFloor, roomFar));
		applyTextures(backWallBox, "models/itbaLogo.jpg");
		

		// The floor
		final Box floorBox = new Box("floor", new Vector3f(), roomSize, roomThick, roomFar);
		room.attachChild(floorBox);
		floorBox.getLocalTranslation().set(new Vector3f(0, -roomSize + distanceFromFloor, 0));
		applyTextures(floorBox, "models/concreteTexture.jpg");
		

		// The roof
		final Box roofBox = new Box("roof", new Vector3f(), roomSize, roomThick, roomFar);
		room.attachChild(roofBox);
		roofBox.getLocalTranslation().set(new Vector3f(0, roomSize + distanceFromFloor, 0));
		applyTextures(roofBox, "models/roofTexture.jpg");
		
		// The right wall
		final Box rightWallBox = new Box("rightWall", new Vector3f(), roomThick, roomSize, roomFar);
		room.attachChild(rightWallBox);
		rightWallBox.getLocalTranslation().set(new Vector3f(roomSize, distanceFromFloor, 0));
		applyTextures(rightWallBox, "models/wallTexture.jpg");
		
		// The left wall
		final Box leftWallBox = new Box("leftWall", new Vector3f(), roomThick, roomSize, roomFar);
		room.attachChild(leftWallBox);
		leftWallBox.getLocalTranslation().set(new Vector3f(-roomSize, distanceFromFloor, 0));
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
		
		this.roomSize = size;
		this.roomThick = thick;
		this.roomFar = far;
	}
	
	private void setupSceneData()
	{
		SapoConfig config = SapoConfig.getInstance();
		
		roomSize = config.getRoomSize();
		roomThick = config.getRoomThick();
		roomFar = config.getRoomFar();
		distanceFromFloor = config.getDistanceFromFloor();
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
