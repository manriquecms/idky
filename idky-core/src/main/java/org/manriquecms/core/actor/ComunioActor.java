package org.manriquecms.core.actor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import lombok.extern.log4j.Log4j2;
import org.manriquecms.core.service.ComunioService;
import org.manriquecms.core.service.impl.ComunioServiceImpl;

@Log4j2
public class ComunioActor extends MyAbstractActor {

    ComunioService comunioService = ComunioServiceImpl.getInstance();

    @Override
    public Receive createReceive() {

        return receiveBuilder()
                .matchEquals("login", p -> {
                    logMessage("Received Login comunio!");
                    comunioService.login();
                })
                .matchAny(o -> logMessage("Received unknown message"))
                .build();
    }


    public static Props props() {
        return Props.create(ComunioActor.class);
    }
}
