package jmetest;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Arrow;
import com.jme.scene.shape.AxisRods;
import com.jme.scene.shape.GeoSphere;
import com.jme.scene.shape.Sphere;
import com.jme.scene.shape.Teapot;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jme.util.resource.MultiFormatResourceLocator;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.physics.DynamicPhysicsNode;

/**
 * <code>SceneRenderTest</code>
 * 
 * This class is to test the principal shapes of the framework JMonkey
 * 
 * @author Hari
 */
public class SceneRenderTest extends SimpleGame
{

	private Quaternion rotQuat = new Quaternion();
	private float angle = 0;
	private Vector3f axis = new Vector3f(1, 1, 0);
	private Sphere s;
	private Arrow a;

	/**
	 * Entry point for the test,
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		SceneRenderTest app = new SceneRenderTest();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	protected void simpleUpdate()
	{
		if (tpf < 1)
		{
			angle = angle + (tpf * 1);
			if (angle > 360)
			{
				angle = 0;
			}
		}
		rotQuat.fromAngleAxis(angle, axis);
		s.setLocalRotation(rotQuat);
	}

	/**
	 * builds the trimesh.
	 * 
	 * @see com.jme.app.SimpleGame#initGame()
	 */
	protected void simpleInitGame()
	{
		display.setTitle("jME - Sphere");

		// Sphere figure Test
		s = new Sphere("Sphere", 63, 50, 25.0F);
		s.setLocalTranslation(new Vector3f(0.0F, 0.0F, -350.0F));
		s.setModelBound(new BoundingBox());
		s.updateModelBound();
		applyTextures(s, "models/woodTexture.jpg");
		rootNode.attachChild(s);

		// Arrorw Figure test
		a = new Arrow("Arrow", 10.0F, 5.0F);
		a.setLocalTranslation(new Vector3f(10.0F, 10.0F, -40.0F));
		a.setModelBound(new BoundingBox());
		a.updateModelBound();
		rootNode.attachChild(a);

		// Teapot figure test
		Teapot t = new Teapot("teapot");
		t.setLocalTranslation(new Vector3f(-10.0F, -10.0F, -40.0F));
		t.setModelBound(new BoundingBox());
		t.updateModelBound();
		applyTextures(t, "models/ironTexture.jpg");
		rootNode.attachChild(t);

		// GeoSphere figure test
		GeoSphere g = new GeoSphere("GeoSphere", true, 1);
		g.setLocalTranslation(new Vector3f(0F, -10.0F, -40.0F));
		g.setModelBound(new BoundingBox());
		g.updateModelBound();
		rootNode.attachChild(g);

		// Create an right handed axisrods object with a scale of 1/2
		AxisRods ar = new AxisRods("rods", true, 0.5f);
		// Attach ar to the node we want to visualize
		rootNode.attachChild(ar);

		
//		try
//		{
//			MultiFormatResourceLocator loc2 = new MultiFormatResourceLocator(new File("c:/")
//				.toURI(), ".jpg", ".png", ".tga");
//			ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, loc2);
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//
//		URL u = ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE,
//			"/model/grass.gif");
//		System.err.println("FOUND URL: " + u);
//
//		TextureState ts = display.getRenderer().createTextureState();
//		ts.setEnabled(true);
//		ts.setTexture(TextureManager.loadTexture(u, Texture.MinificationFilter.Trilinear,
//			Texture.MagnificationFilter.Bilinear));
//
//		rootNode.setRenderState(ts);
//
//		BlendState alpha = display.getRenderer().createBlendState();
//		alpha.setBlendEnabled(true);
//		alpha.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
//		alpha.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
//		alpha.setTestEnabled(true);
//		alpha.setTestFunction(BlendState.TestFunction.GreaterThan);
//		alpha.setEnabled(true);
//		rootNode.setRenderState(alpha);
		
		
		
		
	}
	
	protected void color(Spatial spatial, ColorRGBA color)
	{
		final MaterialState materialState = display.getRenderer().createMaterialState();
		materialState.setDiffuse(color);
		if (color.a < 1)
		{
			final BlendState blendState = display.getRenderer().createBlendState();
			blendState.setEnabled(true);
			blendState.setBlendEnabled(true);
			blendState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
			blendState.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
			spatial.setRenderState(blendState);
			spatial.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
		}
		spatial.setRenderState(materialState);
	}
	
	protected void applyTextures(Spatial node, String texturePath)
	{
		File textures = new File(texturePath);
		try
		{
			SimpleResourceLocator location = new SimpleResourceLocator(textures.toURI().toURL());
			ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, location);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
		URL u = ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE,
			texturePath);

		TextureState ts = display.getRenderer().createTextureState();
		ts.setEnabled(true);
		ts.setTexture(TextureManager.loadTexture(u, Texture.MinificationFilter.BilinearNearestMipMap,
			Texture.MagnificationFilter.Bilinear));
		node.setRenderState(ts);
	}
}