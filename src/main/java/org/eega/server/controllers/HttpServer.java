package org.eega.server.controllers;

import org.eega.server.exceptions.InvalidRequestException;
import org.eega.server.services.RequestResponseProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Ravi Nain on 6/10/2017.
 * HttpServer
 */
@Controller
public class HttpServer {
    private ServerSocket server = null;
    private static final Logger LOGGER = Logger.getLogger(HttpServer.class.getName());
    private boolean running = true;
    @Autowired
    private RequestResponseProcessor requestResponseProcessor;

    /**
     * Start server and listen client request.
     * @param portNumber Input port number
     * @throws IOException throws exception
     */
    public void startServer(final int portNumber) throws IOException {
        server = new ServerSocket(portNumber);
        LOGGER.log(Level.INFO, "Listening for connection on port " + portNumber);

        while (running) {
            try (Socket socket = server.accept();
                 InputStreamReader isr = new InputStreamReader(socket.getInputStream());
                 BufferedReader reader = new BufferedReader(isr);
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                LOGGER.log(Level.FINE, "Request Received");

                try {
                    requestResponseProcessor.processRequest(reader);
                    requestResponseProcessor.sendResponse(writer);
                } catch (final InvalidRequestException e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                    requestResponseProcessor.sendErrorResponse(writer, e.getCode());
                }
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    /**
     * Stop server, not used.
     */
    public void stopServer() {
        this.running = false;
    }
}
