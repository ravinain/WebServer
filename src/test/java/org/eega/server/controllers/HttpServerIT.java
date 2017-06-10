package org.eega.server.controllers;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by Ravi Nain on 6/10/2017.
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class HttpServerIT {

//    @Autowired
    private HttpServer server;

    @Test
    @Ignore
    public void serverShouldReturnOk() {
        server.stopServer();
    }

}