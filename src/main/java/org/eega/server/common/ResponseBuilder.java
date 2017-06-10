package org.eega.server.common;

import org.eega.server.config.ApplicationProperties;
import org.eega.server.domain.HttpRequest;
import org.eega.server.domain.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.eega.server.common.Constants.HEADER_SEPARATOR;
import static org.eega.server.common.Constants.NEXT_LINE;

/**
 * Created by Ravi Nain on 6/10/2017.
 * Response Builder
 */
public class ResponseBuilder {
    private StringBuilder response = new StringBuilder();
    private HttpRequest request = null;
    private HttpResponse httpResponse = null;

    public ResponseBuilder(final HttpRequest request) {
        this.request = request;
    }

    public ResponseBuilder(final HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    /**
     * Append file content.
     * @param filePath Input file path.
     * @return this
     * @throws IOException If file doesn't exists then throws and exception.
     */
    public ResponseBuilder appendFileContent(final String filePath) throws IOException {
        response.append("<pre>");
        Files.lines(Paths.get(filePath))
                .forEach(line -> response.append(line).append(Constants.NEXT_LINE));
        response.append("</pre>");
        return this;
    }

    /**
     * Append response body header.
     * @return this
     */
    public ResponseBuilder appendBodyHeader() {
        response.append("<title>Index of ").append(request.getResource()).append("</title>");
        response.append("<body><h1>Index of ").append(request.getResource()).append("</h1>");
        response.append("<pre>").append("      <a href=''>Name</a>              ")
                .append("<a href=''>Last Modified</a>              ")
                .append("<a href=''>Size</a>              ")
                .append("<a href=''>Description</a>")
                .append("<hr />");

        return this;
    }

    /**
     * Append extra parent record in list to navigate back to parent directory.
     * @return this
     */
    public ResponseBuilder appendParentInList() {
        if (!request.getResource().equals("/")) {
            String root = request.getResource().substring(0, request.getResource().lastIndexOf("/"));
            response.append("      ")
                    .append("<a href='")
                    .append(root.equals("") ? "/" : root)
                    .append("'>..</a>              ")
                    .append("<a href=''></a>              ")
                    .append("<a href=''></a>              ")
                    .append("<a href=''></a>              <br />");
        }
        return this;
    }

    /**
     * Append list of directories in response.
     * @param directoryFile input directory path.
     * @return this
     * @throws IOException If directory doesn't exists then throws an exception.
     */
    public ResponseBuilder appendDirectories(final String directoryFile) throws IOException {
        Files.newDirectoryStream(Paths.get(directoryFile), path -> path.toFile().isDirectory()).forEach(data -> {
            File file = data.toFile();
            String filePath = request.getResource().endsWith("/") ? file.getName() : request.getResource() + "/" + file.getName();
            response.append("      ")
                    .append("<a href='").append(filePath)
                    .append("'>").append(file.getName()).append("/</a>              ")
                    .append("<a href=''>").append(DateUtils.convertMilisecondsToStringDateTime(file.lastModified()))
                    .append("</a>              ")
                    .append("<a href=''>").append(file.length()).append("</a>              ")
                    .append("<a href=''>").append("</a>              <br />");
        });
        return this;
    }

    /**
     * Append list of files in response available in input directory path.
     * @param filePathIn Input directory path.
     * @return this
     * @throws IOException If directory doesn't exists then throws an exception.
     */
    public ResponseBuilder appendFiles(final String filePathIn) throws IOException {
        Files.newDirectoryStream(Paths.get(filePathIn), path -> path.toFile().isFile()).forEach(data -> {
            File file = data.toFile();
            String filePath = request.getResource().endsWith("/") ? file.getName() : request.getResource() + "/" + file.getName();
            response.append("      ")
                    .append("<a href='").append(filePath)
                    .append("'>").append(file.getName()).append("</a>              ")
                    .append("<a href=''>").append(DateUtils.convertMilisecondsToStringDateTime(file.lastModified()))
                    .append("</a>              ")
                    .append("<a href=''>").append(file.length()).append("</a>              ")
                    .append("<a href=''>").append("</a>              <br />");
        });

        return this;
    }

    /**
     * Append footer.
     * @return this
     */
    public ResponseBuilder appendFooter() {
        response.append("<hr/></pre></body>");
        return this;
    }

    /**
     * Append basic info.
     * @return this
     */
    public ResponseBuilder appendBasicInfo() {
        response.append(String.format("%s %d %s\r\n",
                httpResponse.getProtocol(), httpResponse.getStatus().getCode(),
                httpResponse.getStatus().name()));
        return this;
    }

    /**
     * Append default headers in response and skip user specified header.
     * @param headers user specified header.
     * @return this
     */
    public ResponseBuilder appendDefaultHeaders(final Map<String, String> headers) {
        httpResponse.getHeaders().forEach((k, v) -> {
            if (!headers.containsKey(k)) {
                response.append(k).append(HEADER_SEPARATOR).append(v).append(NEXT_LINE);
            }
        });

        return this;
    }

    /**
     * Append user defined header.
     * @param headers user defined header.
     * @return this
     */
    public ResponseBuilder appendUserHeaders(final Map<String, String> headers) {
        headers.forEach((k, v) -> response.append(k).append(HEADER_SEPARATOR).append(v).append(NEXT_LINE));
        return this;
    }

    /**
     * Append response body in response.
     * @return this
     */
    public ResponseBuilder appendResponseBody() {
        response.append(NEXT_LINE).append(httpResponse.getResponseBody());
        return this;
    }

    public String build() {
        return response.toString();
    }
}
