package game;

import game.core.TgosMain;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Game Of Sapo ----------------
 * 
 * This is the main class of the game. From this class, the game is launched and start
 * running.
 * 
 */
public class TgosRunner
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// to see the important stuff
		Logger.getLogger("").setLevel(Level.WARNING); 
		
		// Make a instance of the game
		TgosMain mainApp = new TgosMain();
		
		// Show the configurations
//		mainApp.setConfigShowMode(ConfigShowMode.AlwaysShow);
		
		// Run the game
		mainApp.start();

	}

}
