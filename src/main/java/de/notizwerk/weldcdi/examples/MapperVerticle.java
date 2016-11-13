package de.notizwerk.weldcdi.examples;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import javax.inject.Inject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;


/**
 *
 * @author Tarek El-Sibay
 */
public class MapperVerticle extends AbstractVerticle {

    Logger logger = LoggerFactory.getLogger(MapperVerticle.class);
    
    @Inject
    private Mapper mappper;
    
    @Override
    public void start() throws Exception {
        logger.info("starting MapperVerticle");
        vertx.eventBus().<String>consumer("mapper").handler( (Message<String> msg)->{
            if ( mappper != null ) {
                msg.reply(mappper.map(msg.body()));
            } else {
                msg.reply("NO MAPPER");
            }
        });
        
    }
    
    
}
