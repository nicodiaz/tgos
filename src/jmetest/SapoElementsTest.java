package jmetest;

import game.elements.SapoLittle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.model.converters.X3dToJme;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.util.SimplePhysicsGame;

/**
 * <code>SceneRenderTest</code>
 * 
 * This class is to test the principal shapes of the framework JMonkey
 * 
 * @author Hari
 */
public class SapoElementsTest extends SimplePhysicsGame
{

	/**
	 * Entry point for the test,
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		 SapoElementsTest app = new SapoElementsTest();
		 app.start();

	}

	protected void simpleUpdate()
	{
	}

	/**
	 * builds the trimesh.
	 * 
	 * @see com.jme.app.SimpleGame#initGame()
	 */
	protected void simpleInitGame()
	{
		display.setTitle("jME - Cylinder and Boxes");

		StaticPhysicsNode staticNode = getPhysicsSpace().createStaticNode();
		rootNode.attachChild(staticNode);

		// Cylinder c = new Cylinder("Cylinder", 10, 10, 5, 200);
		// c.getLocalRotation().fromAngleNormalAxis(FastMath.PI / 2.0f, new
		// Vector3f(-1,0,0));
		// c.setLocalTranslation(new Vector3f(0.0F, 0.0F, -35.0F));
		// c.setModelBound(new BoundingBox());
		// c.updateModelBound();
		// staticNode.attachChild(c);

		// Node top = (Node) load3ds("models/top.x3d");
		// staticNode.attachChild(top);
		//		
		// staticNode.getLocalRotation().fromAngleNormalAxis(FastMath.PI / 2.0f,
		// new Vector3f(-1, 0, 0));
		// staticNode.getLocalScale().set(1f, 1f, 1f);
		// staticNode.getLocalTranslation().set(new Vector3f(0, 0, 0));

		// final Box b = new Box("box", new Vector3f(), 20, 0.20f, 20f);
		// staticNode.attachChild(b);

		SapoLittle theSapo = new SapoLittle(getPhysicsSpace(), rootNode, display, new Vector3f());
		StaticPhysicsNode lsStaticNode = theSapo.getSapoLittleStaticNode();
		
		staticNode.generatePhysicsGeometry();
		
		
		
//		Coin coin = new Coin(getPhysicsSpace(), rootNode, display);
		

		// DynamicPhysicsNode dynamicNode = getPhysicsSpace().createDynamicNode();
		// rootNode.attachChild(dynamicNode);
		// final Box f = new Box("falling box", new Vector3f(), 1, 1, 1);
		// dynamicNode.attachChild(f);
		// dynamicNode.generatePhysicsGeometry();
		// dynamicNode.getLocalTranslation().set(new Vector3f(65, 80, -30));

		pause = true;

	}

	public static Spatial load3ds(String modelPath, String textureDir)
	{
		Spatial output = null; // the geometry will go here.
		// Geometry output = null;

		/*
		 * byte array streams don't have to be closed
		 */
		final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		try
		{
			final File textures;
			if (textureDir != null)
			{ // set textureDir location
				textures = new File(textureDir);
			}
			else
			{// try to infer textureDir from modelPath.
				textures = new File(modelPath.substring(0, modelPath.lastIndexOf('/')));
			} // Add texture URL to auto-locator
			final SimpleResourceLocator location = new SimpleResourceLocator(textures.toURI()
				.toURL());
			ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, location);

			// read .3ds file into memory & convert it to a jME usable format.
			final FileInputStream rawIn = new FileInputStream(modelPath);
			X3dToJme converter = null;
			try
			{
				converter = new X3dToJme();
				converter.convert(rawIn, outStream);
			}
			catch (InstantiationException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rawIn.close(); // FileInputStream s must be explicitly closed.

			// prepare outStream for loading.
			final ByteArrayInputStream convertedIn = new ByteArrayInputStream(outStream
				.toByteArray());

			// import the converted stream to jME as a Spatial
			output = (Spatial) BinaryImporter.getInstance().load(convertedIn);
			// output = (Geometry)BinaryImporter.getInstance().load(convertedIn);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.err.println("File not found at: " + modelPath);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.err.println("Unable read model at: " + modelPath);
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
			System.err.println("Invalid texture location at:" + textureDir);
		}

		// output.setModelBound(new BoundingBox());
		// output.updateModelBound();

		return output;
	}

	public static Spatial load3ds(String modelPath)
	{
		return load3ds(modelPath, null);
	}
}