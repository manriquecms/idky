package org.manriquecms.core.util;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import lombok.extern.log4j.Log4j2;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

@Log4j2
public class MyHttpClient {
    private static HttpClient client;
    public static void get(String uri) throws Exception {
        HttpClient client = getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        HttpResponse<Path> response =
                client.send(request, HttpResponse.BodyHandler.asFile(Paths.get("body.txt")));

        log.info("Response in file:" + response.body());
    }
    
    public static CompletableFuture<String> asyncGet(String uri) {
        HttpClient client = getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandler.asString())
                .thenApply(HttpResponse::body);
    }

    public static void post(String uri, String data) throws Exception {
        HttpClient client = getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .POST(HttpRequest.BodyPublisher.fromString(data))
                .build();

        HttpResponse<?> response = client.send(request, HttpResponse.BodyHandler.discard(""));
        log.info(response.statusCode());
    }

    private static HttpClient getInstance(){
        if (client == null){
            client = HttpClient.newBuilder().build();
        }
        return client;
    }

}
