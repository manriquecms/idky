package org.manriquecms.core.actor;

import akka.actor.*;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ExampleActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("ask_actor2_hello", p -> {
                    logMessage("I have to say hello to actor2");
                    ActorSelection sel = getContext().getSystem().actorSelection("user/exampleActor2");
                    sel.tell("hello", getSelf());
                })
                .matchEquals("hello", p -> {
                    logMessage("Received Hello from "+getSender().path()+"!");
                    getSender().tell("hello_response",ActorRef.noSender());
                })
                .matchEquals("hello_response", p -> {
                    logMessage("Received Hello response!");
                })
                .matchAny(o -> logMessage("Received unknown message"))
                .build();
    }

    public static Props props() {
        return Props.create(ExampleActor.class);
    }

    protected void logMessage(String message){
        log.info("["+getSelf().path()+"] "+message);
    }
}
