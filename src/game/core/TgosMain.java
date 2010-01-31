package game.core;

import game.elements.RoomFactory;

import com.jmex.physics.util.SimplePhysicsGame;

public class TgosMain extends SimplePhysicsGame
{
	
	@Override
	protected void simpleInitGame()
	{
		// We start creating the room
		RoomFactory roomFactory = new RoomFactory(getPhysicsSpace(), rootNode);
		
	}
	

}
