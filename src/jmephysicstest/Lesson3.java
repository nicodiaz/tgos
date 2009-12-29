package jmephysicstest;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.contact.MutableContactInfo;
import com.jmex.physics.material.Material;
import com.jmex.physics.util.SimplePhysicsGame;

public class Lesson3 extends SimplePhysicsGame
{

	public static void main(String[] args)
	{
		Lesson3 app = new Lesson3();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	@Override
	protected void simpleInitGame()
	{
		/*
		 * Creamos el mismo piso de las lecciones 1 y 2. Pero lo rotamos con la
		 * ultima instruccion, para hacer un plano inclinado.
		 */
		StaticPhysicsNode staticNode = getPhysicsSpace().createStaticNode();
		rootNode.attachChild(staticNode);
		final Box visualFloorBox = new Box("floor", new Vector3f(), 5, 0.25f, 5);
		staticNode.attachChild(visualFloorBox);
		/*
		 * El 0.3 es el angulo de inclinacion y el {0,0,-1} quiere decir que lo
		 * rotas en el eje z (hacia la derecha)
		 */
		visualFloorBox.getLocalRotation().fromAngleNormalAxis(0.3f, new Vector3f(0, 0, -1));

		/*
		 * Creamos otra box para el piso. MUY IMPORTANTE: Si bien tenemos que
		 * crear tantos dynamic Node como objetos que interactuen sea necesario
		 * pero SOLO es necesario UN SOLO STATIC NODE, aunque hayan varios
		 * objetos statics.
		 */
		final Box visualFloorBox2 = new Box("floor", new Vector3f(), 5, 0.25f, 5);
		staticNode.attachChild(visualFloorBox2);
		visualFloorBox2.getLocalTranslation().set(9.7f, -1.5f, 0);
		staticNode.generatePhysicsGeometry();

		/*
		 * Ahora hacemos los objetos que van a interactuar, simples usando los
		 * boxes de physics
		 */
		DynamicPhysicsNode dynamicNode = createBox();
		dynamicNode.getLocalTranslation().set(0, 5, 0);

		/*
		 * Y un cubo de hielo!! Seteamos las distintas propiedades.
		 */
		final DynamicPhysicsNode iceQube = createBox();
		iceQube.setMaterial(Material.ICE);

		/*
		 * Como le cambie el material, es necesario computar de nuevo la masa
		 * del objeto. Y tambien le hacemos un color acorde.
		 */
		iceQube.computeMass();
		color(iceQube, new ColorRGBA(0.5f, 0.5f, 0.9f, 0.6f));
		/*
		 * Lo trasladamos.
		 */
		iceQube.getLocalTranslation().set(0, 5, 1.5f);

		/*
		 * IMPORTANTE PARA EL TP: Se puede predefinir los materiales propios,
		 * con el correspondiente mu, densidad, etc. VER ESTO EN EL EJEMPLO
		 * COMPLETO.
		 */
		final Material customMaterial = new Material("supra-stopper");
		customMaterial.setDensity(0.05f);
		MutableContactInfo contactDetails = new MutableContactInfo();
		contactDetails.setBounce(0);
		contactDetails.setMu(1000); // todo: Float.POSITIVE_INFINITY ROMPE LINUX
		customMaterial.putContactHandlingDetails(Material.DEFAULT, contactDetails);

		final DynamicPhysicsNode stopper = createBox();
		stopper.setMaterial(customMaterial);
		stopper.computeMass();
		color(stopper, new ColorRGBA(1, 0, 0, 1));
		stopper.getLocalTranslation().set(0, 5, 3);

		/*
		 * Empezamos pausado para poder ver bien ;)
		 */
		pause = true;
	}

	/**
	 * Little helper method to color a spatial.
	 * 
	 * @param spatial
	 *            the spatial to be colored
	 * @param color
	 *            desired color
	 */
	private void color(Spatial spatial, ColorRGBA color)
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

	/**
	 * Create a box like known from Lesson2.
	 * 
	 * @return a physics node containing a box
	 */
	private DynamicPhysicsNode createBox()
	{
		DynamicPhysicsNode dynamicNode = getPhysicsSpace().createDynamicNode();
		rootNode.attachChild(dynamicNode);
		final Box visualFallingBox = new Box("falling box", new Vector3f(), 0.5f, 0.5f, 0.5f);
		dynamicNode.attachChild(visualFallingBox);
		dynamicNode.generatePhysicsGeometry();
		return dynamicNode;
	}
}
