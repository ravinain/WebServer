package org.eega.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Ravi Nain on 6/10/2017.
 * Application properties.
 */
@ConfigurationProperties(prefix = "app")
@Component
public class ApplicationProperties {

    private String port;
    private String rootDirectory;

    private String characterEncoding;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getRootDirectory() {
        return rootDirectory;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    public void setRootDirectory(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }
}
