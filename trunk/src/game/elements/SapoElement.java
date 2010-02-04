/**
 * 
 */
package game.elements;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import com.jme.image.Texture;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.physics.PhysicsSpace;

/**
 * @author ndiazpai
 *
 */
public abstract class SapoElement
{
	protected PhysicsSpace space = null;
	protected Node rootNode = null;
	protected DisplaySystem display = null;
	
	
	
	public SapoElement(PhysicsSpace space, Node rootNode, DisplaySystem display)
	{
		super();
		this.space = space;
		this.rootNode = rootNode;
		this.display = display;
	}



	protected void applyTextures(Node node, String texturePath)
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
		ts.setTexture(TextureManager.loadTexture(u, Texture.MinificationFilter.Trilinear,
			Texture.MagnificationFilter.Bilinear));
		node.setRenderState(ts);
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

}
