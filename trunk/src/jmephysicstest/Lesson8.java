package jmephysicstest;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.shape.Box;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.PhysicsUpdateCallback;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.contact.ContactInfo;
import com.jmex.physics.material.Material;
import com.jmex.physics.util.SimplePhysicsGame;

/**
 * This lesson shows a very simple example of modelling physics for a jump-and-run-like
 * player character. It uses a box with an altered center of gravity. Moving the player is
 * done with surface motion, jumping by directly applying forces.
 * <p/>
 * Important: this is, of course, not meant as <i>the</i> way to go! The actual physical
 * representation that is chosen for characters in a game strongly depends on the
 * abilities the character should have and the other effects you want to achieve. E.g. you
 * could make a racing game with the approach shown here, but it will disregard many
 * aspects you might want to have in a racing game...
 * <p/>
 * Especially not regarded here: character cannot be turned, rotation of the character is
 * not restricted - it may turn when acting a while.
 * 
 * @author Irrisor
 */
public class Lesson8 extends SimplePhysicsGame
{

	/*
	 * Nodo para el piso.
	 */
	private StaticPhysicsNode floor;

	/*
	 * Nodo para el player.
	 */
	private DynamicPhysicsNode player;

	/*
	 * Variable para conocer el estado del jugador, si está tocando o no el piso.
	 */
	private boolean playerOnFloor = false;

	/**
	 * The main method to allow starting this class as application.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args)
	{
		Logger.getLogger("").setLevel(Level.WARNING); // to see the important stuff
		new Lesson8().start();
	}

	@Override
	protected void simpleInitGame()
	{

		/*
		 * Empezamos armando el piso, con el método creado anteriormente.
		 */
		createFloor();

		/*
		 * Ahora creamos nuestro player, que en este caso va a ser una caja. De paso, lo
		 * pintamos!
		 */
		player = createBox();
		player.setName("player");

		/*
		 * Ahora modificamos un poco el player. Primero, le cambiamo el color. Segundo, lo
		 * trasladamos por encima del piso y por ultimo, le modificamos el centro de masa,
		 * para que se encuentre ubicado abajo de la caja, lo que serían los "pies" del
		 * player.
		 */
		color(player, new ColorRGBA(153 / 255.0F, 255 / 255.0F, 204 / 255.0F, 1F));
		player.getLocalTranslation().set(8, 1, 0);
		player.setCenterOfMass(new Vector3f(0, -0.5f, 0));

		/*
		 * Ahora bien, viene lo nuevo. Para poder mover el personaje, debemos crear un
		 * material nuevo que tenga surface motion.
		 */
		final Material playerMaterial = new Material("player material");
		player.setMaterial(playerMaterial);

		// Mapeamos el movimiento de atrás-adelante con las teclas correspondientes.
		input.addAction(new MoveAction(new Vector3f(-2, 0, 0)), InputHandler.DEVICE_KEYBOARD,
			KeyInput.KEY_PGUP, InputHandler.AXIS_NONE, false);
		input.addAction(new MoveAction(new Vector3f(2, 0, 0)), InputHandler.DEVICE_KEYBOARD,
			KeyInput.KEY_PGDN, InputHandler.AXIS_NONE, false);

		/*
		 * Ahora tenemos que agregar la opción del salto. Para ello, agregamos una tecla
		 * que simplemente imprima una fuerza hacia arriba. Sin embargo, dependiendo el
		 * juego obviamente, habría que verificar que el personaje tiene los pies sobre el
		 * piso para poder realizar el salto!!!
		 */
		input.addAction(new InputAction() {
			public void performAction(InputActionEvent evt)
			{
				if (playerOnFloor && evt.getTriggerPressed())
				{
					player.addForce(new Vector3f(0, 500, 0));
				}
			}
		}, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_SPACE, InputHandler.AXIS_NONE, false);

		/*
		 * Ahora, utilizamos como en la leccion anterior, un detector de colisiones para
		 * saber el momento en el cual el player toca el suelo, para setear en true la
		 * variable correspondiente.
		 */
		SyntheticButton playerCollisionEventHandler = player.getCollisionEventHandler();
		input.addAction(new InputAction() {
			public void performAction(InputActionEvent evt)
			{
				ContactInfo contactInfo = (ContactInfo) evt.getTriggerData();
				if (contactInfo.getNode1() == floor || contactInfo.getNode2() == floor)
				{
					playerOnFloor = true;
				}
			}
		}, playerCollisionEventHandler, false);
		
        // and a very simple callback to set the variable to false before each step
        getPhysicsSpace().addToUpdateCallbacks( new PhysicsUpdateCallback() {
            public void beforeStep( PhysicsSpace space, float time ) {
                playerOnFloor = false;
            }

            public void afterStep( PhysicsSpace space, float time ) {

            }
        } );

        // finally print a key-binding message
        Text infoText = Text.createDefaultTextLabel( "key info", "[page up] and [page down] para moverse, [up] to jump" );
        infoText.getLocalTranslation().set( 0, 20, 0 );
        statNode.attachChild( infoText );
		
	}

	/*
	 * Metodo para crear el piso
	 */
	private void createFloor()
	{
		// Como de costumbre, creamos el static node para luego agregarlo a la raiz
		floor = getPhysicsSpace().createStaticNode();
		rootNode.attachChild(floor);

		// Usamos las box de jme, armadas con trimeshes
		final Box visualFloorBox = new Box("floor", new Vector3f(), 5, 0.25f, 5);
		floor.attachChild(visualFloorBox);
		// and not that steep
		visualFloorBox.getLocalRotation().fromAngleNormalAxis(0.1f, new Vector3f(0, 0, -1));

		// Armamos el otro piso. Observemos que solo utilizamos un static node
		final Box visualFloorBox2 = new Box("floor", new Vector3f(), 5, 0.25f, 5);
		floor.attachChild(visualFloorBox2);
		visualFloorBox2.getLocalTranslation().set(9.7f, -0.5f, 0);

		// ... y otra caja, al mismo static node
		final Box visualFloorBox3 = new Box("floor", new Vector3f(), 5, 0.25f, 5);
		floor.attachChild(visualFloorBox3);
		visualFloorBox3.getLocalTranslation().set(-11, 0, 0);

		/*
		 * Como los piso fueron generados por una primitiva de jme, por trimeshes, es
		 * necessario armar la geometria del mismo, mediante la siguiente instruccion.
		 */
		floor.generatePhysicsGeometry();
	}

	/*
	 * Con este metodo, creamos cajas. Es util para cuando es necesario crear multiples
	 * cajas, y de esta forma queda el código factoreado. Para más detalle, ver la
	 * Lesson2.
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

	/*
	 * Metodo para facilitar el coloreo de un "spatial", mediante un RGB
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

	/*
	 * Clase para mapear los movimientos a través de las teclas.
	 */
	private class MoveAction extends InputAction
	{
		/**
		 * Se debe almacenar la dirección seteada.
		 */
		private Vector3f direction;

		public MoveAction(Vector3f direction)
		{
			this.direction = direction;
		}

		public void performAction(InputActionEvent evt)
		{
			if (evt.getTriggerPressed())
			{
				// key goes down - apply motion
				player.getMaterial().setSurfaceMotion(direction);
			}
			else
			{
				// key goes up - stand still
				player.getMaterial().setSurfaceMotion(new Vector3f(0, 0, 0));
				// note: for a game we usually won't want zero motion on key release but
				// be able to combine
				// keys in some way
			}
		}
	}
	
	@Override
	/*
	 * Reescribimos este metodo, para hacer que la camara siga al personaje permanentemente
	 * (non-Javadoc)
	 * @see com.jme.app.BaseSimpleGame#simpleUpdate()
	 */
    protected void simpleUpdate() {
        // move the cam where the player is
        cam.getLocation().x = player.getLocalTranslation().x;
        cam.update();
    }
}
