package org.manriquecms.core.util;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import lombok.extern.log4j.Log4j2;

import java.net.URI;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Log4j2
public class MyHttpClient {
    private static HttpClient client;
    public static HttpResponse<String>  get(String uri) throws Exception {
        HttpClient client = getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandler.asString());
        log.info(response.statusCode());

        return response;
    }
    
    public static CompletableFuture<String> asyncGet(String uri) {
        HttpClient client = getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandler.asString())
                .thenApply(HttpResponse::body);
    }


    public static HttpResponse<?> post(String uri, String data) throws Exception {
        return post(uri, data, null);
    }

    public static HttpResponse<?> post(String uri, String data, String headers) throws Exception {
        HttpClient client = getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .headers(Optional.ofNullable(headers).orElse(""))
                .POST(HttpRequest.BodyPublisher.fromString(data))
                .build();

        HttpResponse<?> response = client.send(request, HttpResponse.BodyHandler.discard(""));
        log.info(response.statusCode());

        return response;
    }

    private static HttpClient getInstance(){
        if (client == null){
            client = HttpClient.newBuilder().build();
        }
        return client;
    }

}
