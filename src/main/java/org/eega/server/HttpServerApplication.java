package org.eega.server;

import org.eega.server.config.ApplicationProperties;
import org.eega.server.controllers.HttpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Ravi Nain on 6/10/2017.
 */
@SpringBootApplication
public class HttpServerApplication {

    @Autowired
    private ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(HttpServerApplication.class);
    }

    @PostConstruct
    public void init() throws IOException {
        HttpServer httpServer = context.getBean(HttpServer.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);
        httpServer.startServer(Integer.parseInt(properties.getPort()));
    }
}
