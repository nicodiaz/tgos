package jmetest;

import java.io.File;
import java.net.URL;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Arrow;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jme.util.resource.MultiFormatResourceLocator;
import com.jme.util.resource.ResourceLocatorTool;

/**
 * <code>TestSphere</code>
 * @author Mark Powell
 * @version $Id: TestSphere.java 4130 2009-03-19 20:04:51Z blaine.dev $
 */
public class SceneRenderTest extends SimpleGame {

  private Quaternion rotQuat = new Quaternion();
  private float angle = 0;
  private Vector3f axis = new Vector3f(1, 1, 0);
  private Sphere s;
  private Arrow a;

  /**
   * Entry point for the test,
   * @param args
   */
  public static void main(String[] args) {
	SceneRenderTest app = new SceneRenderTest();
    app.setConfigShowMode(ConfigShowMode.AlwaysShow);
    app.start();
  }

  protected void simpleUpdate() {
    if (tpf < 1) {
      angle = angle + (tpf * 1);
      if (angle > 360) {
        angle = 0;
      }
    }
    rotQuat.fromAngleAxis(angle, axis);
    s.setLocalRotation(rotQuat);
  }

  /**
   * builds the trimesh.
   * @see com.jme.app.SimpleGame#initGame()
   */
  protected void simpleInitGame() {
    display.setTitle("jME - Sphere");

    s = new Sphere("Sphere", 63, 50, 25);
    s.setLocalTranslation(new Vector3f(0,0,-40));
    s.setModelBound(new BoundingBox());
    s.updateModelBound();
    rootNode.attachChild(s);

    a = new Arrow("Arrow", 10.0F, 5.0F);
    a.setLocalTranslation(new Vector3f(70, 70, -40));
    a.setModelBound(new BoundingBox());
    a.updateModelBound();
    rootNode.attachChild(a);
    
    try {
        MultiFormatResourceLocator loc2 = new MultiFormatResourceLocator(new File("c:/").toURI(), ".jpg", ".png", ".tga");
        ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, loc2);
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    URL u = ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, "/model/grass.gif");
    System.err.println("FOUND URL: "+u);
    
    TextureState ts = display.getRenderer().createTextureState();
    ts.setEnabled(true);
    ts.setTexture(
        TextureManager.loadTexture(u,
        Texture.MinificationFilter.Trilinear,
        Texture.MagnificationFilter.Bilinear));

    rootNode.setRenderState(ts);
    
    BlendState alpha = display.getRenderer().createBlendState();
    alpha.setBlendEnabled(true);
    alpha.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
    alpha.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
    alpha.setTestEnabled(true);
    alpha.setTestFunction(BlendState.TestFunction.GreaterThan);
    alpha.setEnabled(true);
    rootNode.setRenderState(alpha);
  }
}