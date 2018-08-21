package org.manriquecms.core.runner;

import akka.actor.*;
import akka.http.javadsl.server.HttpApp;
import akka.http.javadsl.server.Route;

import static akka.http.javadsl.server.PathMatchers.integerSegment;
import static akka.http.javadsl.server.PathMatchers.segment;

public class ActorSystemRunner extends HttpApp {

    private static ActorSystemRunner myActorSystemRunner;
    private ActorSystem system;

    private ActorSystemRunner(){
        try {
            system = ActorSystem.create("idky");
        }catch (Exception e ){}
    }

    public static ActorSystemRunner getInstance(){
        if(myActorSystemRunner == null){
            myActorSystemRunner = new ActorSystemRunner();
        }
        return myActorSystemRunner;
    }

    public ActorRef startActor (Props props, String name){
            return system.actorOf(props, name);
    }

    public ActorSystem getSystem(){
            return system;
    }

    @Override
    protected Route routes() {
        return route (
            path(segment("hello"), () ->
                route(
                    get(()  -> complete("Received GET request hello")),
                    put(()  -> complete("Received PUT request hello")),
                    head(() -> complete("Received HEAD request hello"))
                )
            ),
            path(segment("hello").slash(integerSegment()), id ->
                route(
                    get(()  -> complete("Received GET request hello " + id)),
                    put(()  -> complete("Received PUT request hello " + id)),
                    head(() -> complete("Received HEAD request hello " + id))
                )
            ),
            path(segment("comunio"), () ->
                route(
                    get(() -> complete("<h1>hello comunio</h1>"))
                )
            ),
            path(segment("comunio").slash(segment("login")), () ->
                route(
                    get(() -> {
                        ActorSelection sel = system.actorSelection("user/comunioActor1");
                        sel.tell("login", ActorRef.noSender());
                        return complete("<h1>login to comunio</h1>");
                    })
                )
            )
        );

//        return route (
//            path("hello", () ->
//                get(() ->
//                    complete("<h1>Say hello to akka-http broder</h1>")
//                )
//            ),
//            path("comunio", () ->
//                get(() ->
//                    complete("<h1>hello comunio</h1>")
//                )
//            ),
//            path("comunio/login", () ->
//                    get(() -> {
//                        ActorSelection sel = system.actorSelection("user/comunio");
//                        sel.tell("login", ActorRef.noSender());
//                        return complete("<h1>login to comunio</h1>");
//                    })
//            )
//
//        );

    }
}
