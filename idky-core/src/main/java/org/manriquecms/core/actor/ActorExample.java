package org.manriquecms.core.actor;

import akka.actor.*;

public class ActorExample extends AbstractActor {

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

    private void logMessage(String message){
        System.out.println("["+getSelf().path()+"] "+message);
    }

    public static Props props() {
        return Props.create(ActorExample.class);
    }
}
