package de.notizwerk.weldcdi;


import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.impl.verticle.CompilingClassLoader;
import io.vertx.core.spi.VerticleFactory;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import javax.enterprise.inject.Instance;

/**
 *
 * @author Tarek El-Sibay
 */
public class WeldVerticleFactory  implements VerticleFactory {//extends JavaVerticleFactory {

    Logger logger = LoggerFactory.getLogger(WeldVerticleFactory.class);

    public static final String SCANNED_PACKAGES_PROP = "vertx-weld-cdi-scanned-package-classes";
    public static final String RECURSIVE_SCAN_PROP = "vertx-weld-cdi-recursive-scan";
    
        @Override
    public String prefix() {
        return "wcdi";
    }

    Weld weld;
    
    @Override
    public void init(Vertx vertx) {
        logger.info("init weld verticle factory");
        String scannedProperty = System.getProperty(SCANNED_PACKAGES_PROP);
        if ( scannedProperty ==  null || scannedProperty.length() == 0) {
            logger.warn("System property '{0}' is not set. This is not recommended. "
                + "Weld will scan the whole classpath for beans! Please provide a comma separated list of class names,"
                + " indicating the packages to scan. ",SCANNED_PACKAGES_PROP);
            weld = new Weld();
        } else {
            
            weld = new Weld().disableDiscovery();
            String recursiveScan = System.getProperty(RECURSIVE_SCAN_PROP);
            boolean recursive =  (recursiveScan == null || Boolean.valueOf(recursiveScan));
            String[] scannedPackageClasses = scannedProperty.split(",");
            for (String scannedPackageClass : scannedPackageClasses) {
                Class packageClass;
                try {
                    packageClass = vertx.getClass().getClassLoader().loadClass(scannedPackageClass);
                    weld = weld.addPackage(recursive, packageClass);
                } catch (ClassNotFoundException ex) {
                    logger.error("cannot load class '{0}' for preparing weld container.",ex,scannedPackageClass);
                }
            }
        }
    }
    
    private WeldContainer getWeldContainer(ClassLoader classLoader) {
        String id = String.valueOf(classLoader.hashCode());
        WeldContainer weldContainer = WeldContainer.instance(id);
        if ( weldContainer != null ) {
            logger.debug("weldcontainer {0} from cache",id);
            return weldContainer;
        } else {
            logger.debug("init weldcontainer {0}",id);
            return weld.containerId(id).setClassLoader(classLoader).initialize();
        }
    }
    
    @Override
    public Verticle createVerticle(String verticleName, ClassLoader classLoader) throws Exception {
        logger.debug("create weld verticle {0} using classloader {1}",verticleName,classLoader.getClass().getName());
        verticleName = VerticleFactory.removePrefix(verticleName);
        Class clazz;
        if (verticleName.endsWith(".java")) {
            CompilingClassLoader compilingLoader = new CompilingClassLoader(classLoader, verticleName);
            String className = compilingLoader.resolveMainClassName();
            clazz = compilingLoader.loadClass(className);
        } else {
           clazz = classLoader.loadClass(verticleName);
        }
        Instance weldContainer = getWeldContainer(classLoader).instance();
        Instance verticleInstance = weldContainer.select( clazz );
        Verticle verticle = (Verticle) verticleInstance.get();
        logger.debug("verticle class {0}",verticle.getClass().getName());
        return verticle;
    }
   
    
}
