package game.core;

import game.elements.Room;
import game.elements.Sapo;

import com.jmex.physics.util.SimplePhysicsGame;

public class TgosMain extends SimplePhysicsGame
{
	
	@Override
	protected void simpleInitGame()
	{
		// We start creating the room
		Room room = new Room(getPhysicsSpace(), rootNode);
		
		Sapo sapo = new Sapo(getPhysicsSpace(), rootNode);
		
		
	}
	

}
