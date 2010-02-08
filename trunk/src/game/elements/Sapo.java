package game.elements;

import com.jme.bounding.BoundingBox;
import com.jme.intersection.BoundingCollisionResults;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.system.DisplaySystem;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.material.Material;

/**
 * This is the modelling of the Sapo Box, with the inner fallings and outter walls.
 * 
 * @author Hari
 * 
 */
public class Sapo extends SapoElement
{

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

	// Boxes variables
	private Float boxesLength = 2.0f;
	private Float boxesThick = 0.05f;
	private Float boxesHigh = 1.5f;

	// Top Box variables
	private Float topLineDeep = 1.2f;
	private Float topLineThick = boxesThick;
	private Float topTranLinesSeparation = 5.0f;
	private Float topLongLinesSeparation = 5.4f;

	// Other measures
	private Float backWallHeight = 3.0f;
	private Float littleSapoScale = 3.0f;

	/*
	 * Now, begin the internal Sapo variables, to work with the class
	 */
	private StaticPhysicsNode sapoStaticNode = null;

	// For a performance purpose, we must save the index number of this childs.
	private Integer lowerBoxesFloorIndex = 0;
	private Integer middleBoxesFloorIndex = 0;
	private Integer upperBoxesFloorIndex = 0;
	
	// the separation between boxes.
	private Float boxesSeparation = (2 * sapoWidth) / 3.0f;
	
	// And the location of this little sapo, for the camera on it
	SapoLittle sapoLittle = null;
	
	
	/**
	 *  This Enum is to identify every box. Remember, the LOWER_MIDDLE is the SAPO box.
	 */
	public enum Boxes
	{
		LOWER, MIDDLE, UPPER,
		LEFT, RIGHT,
		LOWER_LEFT, SAPO, LOWER_RIGHT,
		MIDDLE_LEFT, MIDDLE_MIDDLE, MIDDLE_RIGHT,
		UPPER_LEFT, UPPER_MIDDLE, UPPER_RIGHT,
		OUT //This is when no box was touched.
	}
	
	
	

	public Sapo(PhysicsSpace theSpace, Node rootNode, DisplaySystem disp)
	{
		super(theSpace, rootNode, disp);

		sapoStaticNode = space.createStaticNode();
		sapoStaticNode.setModelBound(new BoundingBox());
		sapoStaticNode.updateModelBound();
		rootNode.attachChild(sapoStaticNode);

		makeWalls();
		makeInnerFalls();
		makeBoxes();
		makeTop();
		makeFinalDeco();
		makeLittleSapo();

		// The Sapo Box is made in wood
		sapoStaticNode.setMaterial(Material.WOOD);

		sapoStaticNode.setModelBound(new BoundingBox());
		sapoStaticNode.updateModelBound();

		// And finally, generate the require physics
		sapoStaticNode.generatePhysicsGeometry();
	}

	public Sapo(PhysicsSpace theSpace, Node rootNode, DisplaySystem disp, Vector3f center)
	{
		this(theSpace, rootNode, disp);
		sapoStaticNode.getLocalTranslation().set(center);
	}

	/*
	 * Method to make the floor, the back door, and side walls.
	 */
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

		// The right of the box.
		final Box rightSapoWall = new Box("rightSapoWall", new Vector3f(sapoWidth, 0,
			-(sapoZMid - boxesLength)), sapoThick, sapoHeight, ((sapoBack - sapoFront) / 2.0f)
			+ boxesLength);
		sapoStaticNode.attachChild(rightSapoWall);

//		Float incX = (2 * sapoWidth) / 3.0f;
		Float incX = boxesSeparation;
		// The inner right of the box.
		final Box innerRightSapoWall = new Box("innerRightSapoWall", new Vector3f(sapoWidth - incX,
			0, -(sapoZMid - boxesLength)), boxesThick, sapoHeight, ((sapoBack - sapoFront) / 2.0f)
			+ boxesLength);
		sapoStaticNode.attachChild(innerRightSapoWall);

		// The inner left of the box.
		final Box innerLeftSapoWall = new Box("innerLeftSapoWall", new Vector3f(-sapoWidth + incX,
			0, -(sapoZMid - boxesLength)), boxesThick, sapoHeight, ((sapoBack - sapoFront) / 2.0f)
			+ boxesLength);
		sapoStaticNode.attachChild(innerLeftSapoWall);

		// The left of the box.
		final Box leftSapoWall = new Box("leftSapoWall", new Vector3f(-sapoWidth, 0,
			-(sapoZMid - boxesLength)), sapoThick, sapoHeight, ((sapoBack - sapoFront) / 2.0f)
			+ boxesLength);
		sapoStaticNode.attachChild(leftSapoWall);
	}

	/*
	 * Method to create the inner falls, all of them
	 */
	private void makeInnerFalls()
	{

		// We start with the lower fall.
		final Box lowerFall = new Box("lowerFall", new Vector3f(0, 0, 0), sapoWidth,
			sapoHeight * 1.4f, innerFallsThick);
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
		upperFall.getLocalTranslation().set(new Vector3f(0, sapoHeight / 2.0f, -(sapoZMid - incY)));
		sapoStaticNode.attachChild(upperFall);
	}

	/*
	 * Method to create the front boxes, where the coin will fall inside.
	 */
	private void makeBoxes()
	{
		Vector3f lowerPoint = new Vector3f(0, -(sapoHeight - boxesThick),
			-(sapoFront - boxesLength));
		/*
		 * We start with the lower boxes. "Boxes" is a way to say "deposit", because the
		 * idea is make somewhere where the coin will fall. The inner walls will divide
		 * this into boxes.
		 */
		final Box lowerBoxesFloor = new Box("lowerBoxesFloor", lowerPoint, sapoWidth, boxesThick,
			boxesLength);
		sapoStaticNode.attachChild(lowerBoxesFloor);
		lowerBoxesFloorIndex = sapoStaticNode.getChildIndex(lowerBoxesFloor);
		final Box lowerBoxesFront = new Box("lowerBoxesFront", lowerPoint.add(0, boxesHigh,
			boxesLength), sapoWidth, boxesHigh, boxesThick);
		sapoStaticNode.attachChild(lowerBoxesFront);

		/*
		 * Ok. Now, with the others boxes, we have to do the same, changing only the
		 * center point.
		 */
		Vector3f middlePoint = lowerPoint.add(0, (sapoZMid - sapoFront) / 2.0f, 0);
		final Box middleBoxesFloor = new Box("middleBoxesFloor", middlePoint, sapoWidth,
			boxesThick, boxesLength);
		sapoStaticNode.attachChild(middleBoxesFloor);
		middleBoxesFloorIndex = sapoStaticNode.getChildIndex(middleBoxesFloor);
		final Box middleBoxesFront = new Box("middleBoxesFront", middlePoint.add(0, boxesHigh,
			boxesLength), sapoWidth, boxesHigh, boxesThick);
		sapoStaticNode.attachChild(middleBoxesFront);

		/*
		 * Finally, the last one
		 */
		Vector3f upperPoint = lowerPoint.add(0, (sapoZMid - sapoFront), 0);
		final Box upperBoxesFloor = new Box("upperBoxesFloor", upperPoint, sapoWidth, boxesThick,
			boxesLength);
		sapoStaticNode.attachChild(upperBoxesFloor);
		upperBoxesFloorIndex = sapoStaticNode.getChildIndex(upperBoxesFloor);
		final Box upperBoxesFront = new Box("upperBoxesFront", upperPoint.add(0, boxesHigh,
			boxesLength), sapoWidth, boxesHigh, boxesThick);
		sapoStaticNode.attachChild(upperBoxesFront);
	}

	/*
	 * Method responsible of creating the top of the Sapo box. Have the holes and the sapo
	 * itself.
	 */
	private void makeTop()
	{
		makeTransversalTop();
		makeLongitudinalTop();
	}

	/*
	 * This method creates the transversal lines of the top of the Sapo box
	 */
	private void makeTransversalTop()
	{
		// Ths starting point is from the back
		Vector3f transStartingPoint = new Vector3f(0, sapoHeight + topLineThick,
			-(sapoBack - topLineDeep));

		// Last line
		final Box transversalLine4 = new Box("transversalLine4", new Vector3f(), sapoWidth,
			topLineThick, topLineDeep);
		transversalLine4.getLocalTranslation().set(transStartingPoint);
		sapoStaticNode.attachChild(transversalLine4);

		// 3rd Transversal Line
		final Box transversalLine3 = new Box("transversalLine3", new Vector3f(), sapoWidth,
			topLineThick, topLineDeep);
		transversalLine3.getLocalTranslation().set(
			transStartingPoint.add(0, 0, topTranLinesSeparation));
		sapoStaticNode.attachChild(transversalLine3);

		// 2nd Transversal Line
		final Box transversalLine2 = new Box("transversalLine2", new Vector3f(), sapoWidth,
			topLineThick, topLineDeep);
		transversalLine2.getLocalTranslation().set(
			transStartingPoint.add(0, 0, (2.0f * topTranLinesSeparation)));
		sapoStaticNode.attachChild(transversalLine2);

		// first (front) line
		final Box transversalLine1 = new Box("transversalLine1", new Vector3f(), sapoWidth,
			topLineThick, topLineDeep);
		transversalLine1.getLocalTranslation().set(
			transStartingPoint.add(0, 0, (3.0f * topTranLinesSeparation)));
		sapoStaticNode.attachChild(transversalLine1);

	}

	/*
	 * This method creates the longitudinal lines of the top of the Sapo box
	 */
	private void makeLongitudinalTop()
	{
		// Ths starting point is from the back
		Vector3f longStartingPoint = new Vector3f(-(sapoWidth - topLineDeep), sapoHeight
			+ topLineThick, -sapoZMid);

		// first (left) line
		final Box longTopLine1 = new Box("longTopLine1", new Vector3f(), topLineDeep, topLineThick,
			(sapoBack - sapoZMid));
		longTopLine1.getLocalTranslation().set(longStartingPoint);
		sapoStaticNode.attachChild(longTopLine1);

		// 2nd Line line
		final Box longTopLine2 = new Box("longTopLine2", new Vector3f(), topLineDeep, topLineThick,
			(sapoBack - sapoZMid));
		longTopLine2.getLocalTranslation().set(longStartingPoint.add(topLongLinesSeparation, 0, 0));
		sapoStaticNode.attachChild(longTopLine2);

		// 3rd Line line
		final Box longTopLine3 = new Box("longTopLine3", new Vector3f(), topLineDeep, topLineThick,
			(sapoBack - sapoZMid));
		longTopLine3.getLocalTranslation().set(
			new Vector3f(-(longStartingPoint.x + topLongLinesSeparation), longStartingPoint.y,
				longStartingPoint.z));
		sapoStaticNode.attachChild(longTopLine3);

		// 4th (last) Line line
		final Box longTopLine4 = new Box("longTopLine4", new Vector3f(), topLineDeep, topLineThick,
			(sapoBack - sapoZMid));
		longStartingPoint.x = sapoWidth - topLineDeep; // the inverse of the first point
		longTopLine4.getLocalTranslation().set(longStartingPoint);
		sapoStaticNode.attachChild(longTopLine4);
	}

	/*
	 * This method do the final things, like the final walls and other things.
	 */
	private void makeFinalDeco()
	{

		// The upper front, that cover the left holes. This covers the boxes.
		final Box frontTop = new Box("frontTop", new Vector3f(), sapoWidth, topLineThick,
			2.0f * boxesLength);
		frontTop.getLocalTranslation().set(
			new Vector3f(0, (sapoHeight + topLineThick), -(sapoFront)));
		sapoStaticNode.attachChild(frontTop);

		// The front, that cover the front.
		final Box frontFront = new Box("frontFront", new Vector3f(), sapoWidth, 1.5f * boxesHigh,
			boxesThick);
		frontFront.getLocalTranslation().set(
			new Vector3f(0, (sapoHeight - 1.5f * boxesHigh), -(sapoFront - 2.0f * boxesLength)));
		sapoStaticNode.attachChild(frontFront);

		// The upper back wall.
		final Box upperBackSapoWall = new Box("upperBackSapoWall", new Vector3f(0, sapoHeight
			+ backWallHeight, -(sapoBack + (sapoThick / 2.0f))), sapoWidth, backWallHeight,
			sapoThick);
		sapoStaticNode.attachChild(upperBackSapoWall);

		// The upper left Wall
		final Box upperLeftSapoWall = new Box("upperLeftSapoWall", new Vector3f(-sapoWidth,
			sapoHeight + backWallHeight, -(sapoZMid - boxesLength)), sapoThick, backWallHeight,
			((sapoBack - sapoFront) / 2.0f) + boxesLength);
		sapoStaticNode.attachChild(upperLeftSapoWall);

		// The upper right of the box.
		final Box upperRightSapoWall = new Box("rightSapoWall", new Vector3f(sapoWidth, sapoHeight
			+ backWallHeight, -(sapoZMid - boxesLength)), sapoThick, backWallHeight,
			((sapoBack - sapoFront) / 2.0f) + boxesLength);
		sapoStaticNode.attachChild(upperRightSapoWall);

		// The texture...
		applyTextures(sapoStaticNode, "models/woodTexture.jpg");

	}

	private void makeLittleSapo()
	{
		sapoLittle = new SapoLittle(space, rootNode, display, new Vector3f());
		StaticPhysicsNode sl_staticNode = sapoLittle.getSapoLittleStaticNode();
		sapoStaticNode.attachChild(sl_staticNode);
		sl_staticNode.getLocalTranslation().set(0, sapoHeight + (littleSapoScale * sapoLittle.getHeight()),
			-(sapoBack - littleSapoScale * sapoLittle.getWidth() * 1.2f));
		sl_staticNode.getLocalScale().set(littleSapoScale, littleSapoScale, littleSapoScale);
	}

	/**
	 * This method is to recover the points that the player have done.
	 * @param coin The throwed coin.
	 * @return The number indication the points. If no point was made, then return 0.
	 */
	public Integer getPoints(Coin coin)
	{
		Boxes boxTouched = isTouching(coin);
		Integer points = 0;
		
		switch (boxTouched)
		{
		case LOWER_LEFT:
			points = 100;
			break;
		case SAPO:
			points = 150;
			break;
		case LOWER_RIGHT:
			points = 100;
			break;
		case MIDDLE_LEFT:
			points = 80;
			break;
		case MIDDLE_MIDDLE:
			points = 100;
			break;
		case MIDDLE_RIGHT:
			points = 80;
			break;
		case UPPER_LEFT:
			points = 60;
			break;
		case UPPER_MIDDLE:
			points = 80;
			break;
		case UPPER_RIGHT:
			points = 60;
			break;
		case OUT:
		default:
			points = 0;
			break;
		}
		
		return points;
	}
	
	
	public Boxes isTouching(Coin coin)
	{
		Spatial anotherObject = coin.getPhysicCoin();
		Boxes result = null;
		
		/*
		 * The engine have a bug. If i use the getChild with names and detect collisions,
		 * the method is not working. The same happen if I use the node. The only way to
		 * have this working, is recovering the child by the integer. Also, for a
		 * performance issue, is better recover the child by the index, because is O(1).
		 * Otherwise, has O(N).
		 */

		if (sapoStaticNode.getChild(lowerBoxesFloorIndex).hasCollision(anotherObject, false))
		{
			result = getTouchedBox(coin);
			switch (result)
			{
				case LEFT:
					return Boxes.LOWER_LEFT;
				case MIDDLE:
					return Boxes.SAPO;
				case RIGHT:
					return Boxes.LOWER_RIGHT;
				default:
					return Boxes.OUT;
			}
		}

		if (sapoStaticNode.getChild(middleBoxesFloorIndex).hasCollision(anotherObject, false))
		{
			result = getTouchedBox(coin);
			switch (result)
			{
				case LEFT:
					return Boxes.MIDDLE_LEFT;
				case MIDDLE:
					return Boxes.MIDDLE_MIDDLE;
				case RIGHT:
					return Boxes.MIDDLE_RIGHT;
				default:
					return Boxes.OUT;
			}
		}

		if (sapoStaticNode.getChild(upperBoxesFloorIndex).hasCollision(anotherObject, false))
		{
			result = getTouchedBox(coin);
			switch (result)
			{
				case LEFT:
					return Boxes.UPPER_LEFT;
				case MIDDLE:
					return Boxes.UPPER_MIDDLE;
				case RIGHT:
					return Boxes.UPPER_RIGHT;
				default:
					return Boxes.OUT;
			}
		}
		
		return Boxes.OUT;
	}
	
	private Boxes getTouchedBox(Coin coin)
	{
		Vector3f coinPosition = coin.getLocation();
		Vector3f start = new Vector3f(-sapoWidth, 0, 0);
		Vector3f limit = start.add(boxesSeparation, 0, 0);
		
		// Now, we must see wich box has touched. we start with the leftmost box.
		if (coinPosition.x > start.x && coinPosition.x < limit.x)
		{
			return Boxes.LEFT;
		}
		
		// The middle box
		start = limit;
		limit = limit.add(boxesSeparation, 0, 0);
		if (coinPosition.x > start.x && coinPosition.x < limit.x)
		{
			return Boxes.MIDDLE;
		}
		
		// If the cases before fails, is the right box
		start = limit;
		limit = limit.add(boxesSeparation, 0, 0);
		if (coinPosition.x > start.x && coinPosition.x < limit.x)
		{
			return Boxes.RIGHT;
		}
		
		return Boxes.OUT;
	}
	
	public Vector3f getLittleSapoPosition()
	{
		return sapoLittle.getLocation();
	}
}
