package game;

import game.core.TgosMain;

import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.app.AbstractGame.ConfigShowMode;

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
		
		File imgPresen = new File("models/gamePresen.jpg");
		
		// Show the configurations
		try
		{
			mainApp.setConfigShowMode(ConfigShowMode.AlwaysShow, imgPresen.toURI().toURL());
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		
		// Run the game
		mainApp.start();

	}

}
