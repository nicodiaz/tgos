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
	/*
	 * Little Sapo
	 */
	@XStreamAlias("littleWidth")
	private float littleWidth;
	@XStreamAlias("littleHeight")
	private float littleHeight;
	@XStreamAlias("littleThick")
	private float littleThick;

	/*
	 * Coin
	 */
	@XStreamAlias("coinScale")
	private float coinScale;

	/*
	 * Room
	 */
	@XStreamAlias("roomSize")
	private float roomSize;
	@XStreamAlias("roomThick")
	private float roomThick;
	@XStreamAlias("roomFar")
	private float roomFar;
	@XStreamAlias("distanceFromFloor")
	private float distanceFromFloor;

	/*
	 * Sapo
	 */
	@XStreamAlias("sapoWidth")
	private float sapoWidth;
	@XStreamAlias("sapoHeight")
	private float sapoHeight;
	@XStreamAlias("sapoThick")
	private float sapoThick;
	@XStreamAlias("sapoBack")
	private float sapoBack;
	@XStreamAlias("sapoFront")
	private float sapoFront;
	@XStreamAlias("innerFallsThick")
	private float innerFallsThick;
	@XStreamAlias("boxesLength")
	private float boxesLength;
	@XStreamAlias("boxesThick")
	private float boxesThick;
	@XStreamAlias("boxesHigh")
	private float boxesHigh;
	@XStreamAlias("topLineDeep")
	private float topLineDeep;
	@XStreamAlias("topLineThick")
	private float topLineThick;
	@XStreamAlias("topTranLinesSeparation")
	private float topTranLinesSeparation;
	@XStreamAlias("topLongLinesSeparation")
	private float topLongLinesSeparation;
	@XStreamAlias("backWallHeight")
	private float backWallHeight;
	@XStreamAlias("littleSapoScale")
	private float littleSapoScale;

	private static SapoConfig instance = null;

	private SapoConfig()
	{
		// Default
	}

	public static SapoConfig getInstance()
	{
		if (instance != null)
		{
			return instance;
		}

		XStream xstream;
		xstream = new XStream();
		xstream.processAnnotations(SapoConfig.class);

		try
		{
			instance = (SapoConfig) xstream.fromXML(new FileInputStream("scene.xml"));
		}
		catch (FileNotFoundException e)
		{
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

	/**
	 * @return the roomSize
	 */
	public float getRoomSize()
	{
		return roomSize;
	}

	/**
	 * @return the roomThick
	 */
	public float getRoomThick()
	{
		return roomThick;
	}

	/**
	 * @return the roomFar
	 */
	public float getRoomFar()
	{
		return roomFar;
	}

	/**
	 * @return the distanceFromFloor
	 */
	public float getDistanceFromFloor()
	{
		return distanceFromFloor;
	}

	/**
	 * @return the sapoWidth
	 */
	public float getSapoWidth()
	{
		return sapoWidth;
	}

	/**
	 * @return the sapoHeight
	 */
	public float getSapoHeight()
	{
		return sapoHeight;
	}

	/**
	 * @return the sapoThick
	 */
	public float getSapoThick()
	{
		return sapoThick;
	}

	/**
	 * @return the sapoBack
	 */
	public float getSapoBack()
	{
		return sapoBack;
	}

	/**
	 * @return the sapoFront
	 */
	public float getSapoFront()
	{
		return sapoFront;
	}

	/**
	 * @return the innerFallsThick
	 */
	public float getInnerFallsThick()
	{
		return innerFallsThick;
	}

	/**
	 * @return the boxesLength
	 */
	public float getBoxesLength()
	{
		return boxesLength;
	}

	/**
	 * @return the boxesThick
	 */
	public float getBoxesThick()
	{
		return boxesThick;
	}

	/**
	 * @return the boxesHigh
	 */
	public float getBoxesHigh()
	{
		return boxesHigh;
	}

	/**
	 * @return the topLineDeep
	 */
	public float getTopLineDeep()
	{
		return topLineDeep;
	}

	/**
	 * @return the topLineThick
	 */
	public float getTopLineThick()
	{
		return topLineThick;
	}

	/**
	 * @return the topTranLinesSeparation
	 */
	public float getTopTranLinesSeparation()
	{
		return topTranLinesSeparation;
	}

	/**
	 * @return the topLongLinesSeparation
	 */
	public float getTopLongLinesSeparation()
	{
		return topLongLinesSeparation;
	}

	/**
	 * @return the backWallHeight
	 */
	public float getBackWallHeight()
	{
		return backWallHeight;
	}

	/**
	 * @return the littleSapoScale
	 */
	public float getLittleSapoScale()
	{
		return littleSapoScale;
	}

}
