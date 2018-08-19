package org.manriquecms.core.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class RunActorSystem {
    public static void main(String args[]){
        final ActorSystem system = ActorSystem.create("test-system");
        Runtime.getRuntime().addShutdownHook(new TerminatedThread(system));


        ActorRef actor1Ref = system.actorOf(ActorExample.props(), "exampleActor1");
        ActorRef actor2Ref = system.actorOf(ActorExample.props(), "exampleActor2");
        actor1Ref.tell("ask_actor2_hello", ActorRef.noSender());

    }
}

class TerminatedThread extends Thread {
    ActorSystem system;

    public TerminatedThread(ActorSystem system) {
        this.system = system;
    }

    @Override
    public void run() {
        System.out.println("Ending AKKA actor system");
        system.terminate();
    }

}
