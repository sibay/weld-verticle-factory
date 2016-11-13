package de.notizwerk.weldcdi.examples;

import de.notizwerk.weldcdi.WeldVerticleFactory;
import io.vertx.core.AsyncResult; 
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;


/**
 *
 * @author Tarek El-Sibay
 */
public class Bootstrap {
    
    static Logger logger = LoggerFactory.getLogger(Bootstrap.class);
    
    public static void main(String[] args) throws InterruptedException {
        System.setProperty(WeldVerticleFactory.SCANNED_PACKAGES_PROP,"de.notizwerk.weldcdi.examples.Bootstrap");
        System.setProperty(WeldVerticleFactory.RECURSIVE_SCAN_PROP,"true"); // is true by default

        Vertx vertx = Vertx.vertx();
        int instances = 2;
        DeploymentOptions dop = new DeploymentOptions().setInstances(instances);
        Future completion1 = Future.future();
        Future completion2 = Future.future();
        vertx.deployVerticle("wcdi:de.notizwerk.weldcdi.examples.MapperVerticle",dop, async->{
            if (async.succeeded()) {
                logger.info("mapper deployed, sending Hello. {0}",async.result());
                vertx.eventBus().send("mapper", "Hello",(AsyncResult<Message<String>> asyncMsg) -> {
                    if (asyncMsg.succeeded()) {
                        logger.info("Mapper response: {0}",asyncMsg.result().body());
                    } else {
                        logger.error("sending to Mapper failed ",asyncMsg.cause());
                    }
                    completion1.complete();
                });
            } else {
                logger.error("cannot deploy verticle",async.cause());
                completion1.fail(async.cause());
            }
        });

        vertx.deployVerticle("wcdi:de.notizwerk.weldcdi.examples.CalculatorVerticle",dop, async->{
            if (async.succeeded()) {
                logger.info("calculator deployed, sending Hello. {0}",async.result());
                vertx.eventBus().send("calculator", "Hello",(AsyncResult<Message<String>> asyncMsg) -> {
                    if (asyncMsg.succeeded()) {
                        logger.info("Calculator response: {0}",asyncMsg.result().body());
                    } else {
                        logger.error("sending to Calculator failed ",asyncMsg.cause());
                    }
                    completion2.complete();
                });
            } else {
                logger.error("cannot deploy verticle ",async.cause());
                completion2.fail(async.cause());
            }
        });
        
        CompositeFuture.all(completion1,completion2).setHandler( v -> {
            vertx.close();  
        });
    }
}
