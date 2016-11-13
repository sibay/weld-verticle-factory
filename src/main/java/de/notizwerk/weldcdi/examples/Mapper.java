package de.notizwerk.weldcdi.examples;

import javax.enterprise.context.ApplicationScoped;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 *
 * @author Tarek El-Sibay
 */
@ApplicationScoped
public class Mapper {

    Logger logger = LoggerFactory.getLogger(Mapper.class);

    public Mapper() {
        logger.info("Mapper constructor");
    }

    
    public String map(String key) {
        return "map("+key+")";
    }
}
