package com.cmad.vertx;

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;

public class QAForumMainVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> future) throws Exception {
        List<String> clients = new ArrayList<String>();

        EventBus eventBus = vertx.eventBus();

        HttpServer server = vertx.createHttpServer();

        server.websocketHandler(ws -> {
            System.out.println("From Sender CLient ID " + ws.textHandlerID());
            ws.handler(data -> {
                for (String client : clients) {
                    if (!client.equals(ws.textHandlerID())) {
                        System.out.println("Sending to .. " + client + " from "
                                + ws.textHandlerID());
                        eventBus.publish(client, data.toString());
                    }
                }
            });

            System.out.println("From Receiver CLient ID " + ws.textHandlerID());
            clients.add(ws.textHandlerID());
            System.out.println(clients);

        });
        server.listen(8080);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}