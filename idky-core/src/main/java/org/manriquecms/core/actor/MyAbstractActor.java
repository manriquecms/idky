package org.manriquecms.core.actor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MyAbstractActor extends AbstractActor {
    public Receive createReceive() {
        return receiveBuilder().build();
    }

    protected void logMessage(String message){
        log.info("["+getSelf().path()+"] "+message);
    }
}
