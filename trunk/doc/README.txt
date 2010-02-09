******************************************
1. Instrucciones para el deploy de fuentes
******************************************

Las instrucciones, si bien se muestran para una consola de GNU/Linux, 
son idénticas para el deploy en sistemas Windows. En el linkeo de las
bibliotecas nativas, se supone la utilizacion de la IDE de Eclipse.

1.1 Checkout del proyecto TGOS (The Game Of Sapo)

- Lo realizamos mediante el comando:

$svn checkout http://tgos.googlecode.com/svn/trunk/ tgos


1.2 Checkout del proyecto de JME

- Realizar el checkout del repositorio de JMonkey para obtener la ultima 
- revision del framework:

$svn co http://jmonkeyengine.googlecode.com/svn/trunk jme


1.3 Compilacion de los fuentes

- La compilacion se realiza mediante la herramienta de Apache Ant

$cd jme
$ant dist-test
  
  
1.4 Checkout del proyecto JMEPhysics

- Realizar el checkout del repositorio de JMonkeyPhysics para obtener la ultima 
- revision del framework:

svn checkout http://jmephysics.googlecode.com/svn/trunk/ jmephysics


1.5 Compilacion de los fuentes

- La compilacion se realiza mediante la herramienta de Apache Ant

$cd jmephysics/ant
$ant release.all
  
  
1.6 Referencia al classpath con bibliotecas nativas de lwjgl

- Si bien con lo anterior, ya puede compilarse los proyectos con el framework, 
- es necesario linkear las bibliotecas dinamicas (dlls en Windows, .so en *nix).
- Para ello, en Eclipse, vamos a Configure Build Path, "Add JARs", agragamos 
- todos los jar creados en el paso anterior, y buscamos "lwjgl.jar". Esta
- biblioteca es la que contiene el codigo a bajo nivel de la implementacion de 
- OpenGL. Expandimos, y en "Native Library Location" agregamos: 
- "jme/lib/lwjgl/native/linux".


1.7 Referencia al classpath con bibliotecas nativas de ODE

- Repetimos al caso anterior, pero esta vez agregamos al classpath la bilioteca
- "jme-physics.jar" ubicada en "jmephysics/release" y la biblioteca 
- "odejava-jni.jar" ubicada en "jmephysics/imp/ode/lib". A esta ultima, tenemos
- que indicar la referencia con las bibliotecas nativas. Repitiendo el paso
- anterior, a la biblioteca odejava-jni.jar indicamos como nativas 
- "jmephysics/impl/ode/lib".

1.8 Referencia al classpath con bibliotecas nativas de JBullet y Jorbis

- Igual que los dos pasos anteriores, pero agregando 
- "jmephysics/impl/jbullet/lib/jbullet.jar". 

1.9 Referencias a las bibliotecas locales del proyecto.

- Agregar las bibliotecsa encontradas en tgos/lib. Ellas son:
- xpp3_min-VERSION.jar (parser del XML)
- xpp3_min-VERSION.jar (parser de XML)
- jorbis-VERSION.jar (sonido)


