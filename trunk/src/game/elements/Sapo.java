package game.elements;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Torus;
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

	public Sapo(PhysicsSpace theSpace, Node rootNode)
	{
		space = theSpace;
		this.rootNode = rootNode;

		sapoStaticNode = space.createStaticNode();
		sapoStaticNode.setModelBound(new BoundingBox());
		sapoStaticNode.updateModelBound();
		rootNode.attachChild(sapoStaticNode);

		makeWalls();
		makeInnerFalls();
		makeBoxes();
		makeTop();
		makeFinalDeco();

		sapoStaticNode.setModelBound(new BoundingBox());
		sapoStaticNode.updateModelBound();

		// And finally, generate the require physics
		sapoStaticNode.generatePhysicsGeometry();
	}

	public Sapo(PhysicsSpace theSpace, Node rootNode, Vector3f center)
	{
		this(theSpace, rootNode);
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

		Float incX = (2 * sapoWidth) / 3.0f;
		// The inner right of the box.
		final Box innerRightSapoWall = new Box("innerRightSapoWall", new Vector3f(sapoWidth - incX,
			0, -(sapoZMid - boxesLength)), boxesThick, sapoHeight, ((sapoBack - sapoFront) / 2.0f)
			+ boxesLength);
		sapoStaticNode.attachChild(innerRightSapoWall);

		// The inner right of the box.
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

		// The front, that cover the left holes. This covers the boxes.
		final Box frontTop = new Box("frontTop", new Vector3f(), sapoWidth, topLineThick,
			2.0f * boxesLength);
		frontTop.getLocalTranslation().set(
			new Vector3f(0, (sapoHeight + topLineThick), -(sapoFront)));
		sapoStaticNode.attachChild(frontTop);

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

	}

}
