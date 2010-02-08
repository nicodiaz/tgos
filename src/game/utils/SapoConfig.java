package game.utils;


import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Singleton Class to acccess the scene information from the XML file.
 * 
 * @author Hari
 *
 */
@XStreamAlias("scene")
public class SapoConfig
{
	@XStreamAlias("littleWidth")
	private float littleWidth;
	@XStreamAlias("littleHeight")
	private float littleHeight;
	@XStreamAlias("littleThick")
	private float littleThick;
	
	@XStreamAlias("coinScale")
	private float coinScale;
	
	private static SapoConfig instance = null;
	private SapoConfig()
	{
		// Default
	}
	
	public static SapoConfig getInstance() {
		if (instance != null) {
			return instance;
		}

		XStream xstream;
		xstream = new XStream();
		xstream.processAnnotations(SapoConfig.class);

		try {
			instance = (SapoConfig) xstream.fromXML(new FileInputStream("scene.xml"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return instance;
	}

	/**
	 * @return the littleWidth
	 */
	public float getLittleWidth()
	{
		return littleWidth;
	}

	/**
	 * @return the littleHeight
	 */
	public float getLittleHeight()
	{
		return littleHeight;
	}

	/**
	 * @return the littleThick
	 */
	public float getLittleThick()
	{
		return littleThick;
	}

	/**
	 * @return the coinScale
	 */
	public float getCoinScale()
	{
		return coinScale;
	}
	
	

}
