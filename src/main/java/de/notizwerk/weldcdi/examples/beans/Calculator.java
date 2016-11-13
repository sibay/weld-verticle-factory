package de.notizwerk.weldcdi.examples.beans;

import de.notizwerk.weldcdi.examples.Mapper;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;


/**
 *
 * @author Tarek El-Sibay
 */
@ApplicationScoped
public class Calculator {

    Logger logger = LoggerFactory.getLogger(Calculator.class);

    public Calculator() {
        logger.info("Calculator constructor");
    }
    
    @Inject
    private Mapper mapper;

    public Mapper getMapper() {
        return mapper;
    }

    public int calculate(String m) {
        return this.mapper.map(m).hashCode();
    }
    
}
