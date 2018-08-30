package org.manriquecms.core.service.impl;

import jdk.incubator.http.HttpResponse;
import lombok.extern.log4j.Log4j2;
import org.manriquecms.core.service.ComunioService;
import org.manriquecms.core.util.MyHttpClient;

@Log4j2
public class ComunioServiceImpl implements ComunioService {

    private static ComunioServiceImpl myService;
    private final String loginFormData = "login=cmanrique&pass=vCdEr862&action=login&%3E%3E+Login_x=33";

    @Override
    public void login() {
        try {
            HttpResponse<String> getResponse = MyHttpClient.get("http://www.comunio.es/login.phtml");
            //TODO: Get cookie headers and pass to post
//            String getHeaders = getResponse.headers().map().entrySet().stream()
//                    .filter(e -> e.getKey().equalsIgnoreCase("set-cookie"))
//                    .map(e -> e.getKey())
            HttpResponse<?> postResponse = MyHttpClient.post("http://www.comunio.es/login.phtml", loginFormData, getResponse.headers().toString());
            postResponse.headers().map().entrySet().stream()
                    .forEach(e ->
                            log.info(
                                    String.join("=",
                                            e.getKey(),
                                            String.join(",",e.getValue()))));

        }catch (Exception e) {

        }
    }

    public static ComunioServiceImpl getInstance(){
        if (myService == null) {
            myService = new ComunioServiceImpl();
        }
        return myService;
    }
}
