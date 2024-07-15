package rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        RestTemplate restTemplate = new RestTemplate();
        String res = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random",String.class);
        log.info(res);
    }

}