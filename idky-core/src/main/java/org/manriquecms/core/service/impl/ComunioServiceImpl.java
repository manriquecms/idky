package org.manriquecms.core.service.impl;

import lombok.extern.log4j.Log4j2;
import org.manriquecms.core.service.ComunioService;
import org.manriquecms.core.util.MyHttpClient;

@Log4j2
public class ComunioServiceImpl implements ComunioService {

    private static ComunioServiceImpl myService;

    @Override
    public void login() {
        try {
            MyHttpClient.get("http://www.comunio.es/login.phtml");
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
