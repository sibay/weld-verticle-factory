package de.notizwerk.weldcdi.examples;

import de.notizwerk.weldcdi.examples.beans.Calculator;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import javax.inject.Inject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;


/**
 *
 * @author Tarek El-Sibay
 */
public class CalculatorVerticle extends AbstractVerticle {

    Logger logger = LoggerFactory.getLogger(CalculatorVerticle.class);
    
    @Inject
    private Calculator calculator;
    
    @Override
    public void start() throws Exception {
        logger.info("starting CalculatorVerticle");
        vertx.eventBus().<String>consumer("calculator").handler( (Message<String> msg)->{
            if ( calculator != null ) {
                msg.reply(calculator.calculate(msg.body()));
            } else {
                msg.reply("NO CALCULATOR");
            }
        });
        
    }
    
    
}
