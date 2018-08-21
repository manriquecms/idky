package org.manriquecms.core;

import lombok.extern.log4j.Log4j2;
import org.manriquecms.core.actor.ComunioActor;
import org.manriquecms.core.actor.ExampleActor;
import org.manriquecms.core.runner.ActorSystemRunner;

@Log4j2
public class RunActorSystem {
    public static void main(String args[]){
        log.info("Running actor system");
        ActorSystemRunner actorSystemRunner = ActorSystemRunner.getInstance();
        log.info("Initialazing shutdown hook");
        Runtime.getRuntime().addShutdownHook(new TerminatedThread(actorSystemRunner));

        log.info("Creating actors");
        actorSystemRunner.startActor(ExampleActor.props(), "exampleActor1");
        actorSystemRunner.startActor(ExampleActor.props(), "exampleActor2");
        actorSystemRunner.startActor(ComunioActor.props(), "comunioActor1");

        //actor1Ref.tell("ask_actor2_hello", ActorRef.noSender());
        try {
            log.info("Starting server");
            actorSystemRunner.startServer("localhost", 8080);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

class TerminatedThread extends Thread {
    ActorSystemRunner actorSystemRunner;

    public TerminatedThread(ActorSystemRunner actorSystemRunner) {
        this.actorSystemRunner = actorSystemRunner;
    }

    @Override
    public void run() {
        System.out.println("Ending AKKA actor system");
        actorSystemRunner.getSystem().terminate();
    }

}
