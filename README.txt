Web Server
----------

Version
-------
1.0.0

Description
-----------

A simple WebServer which accepts GET request only.

Prerequisite
------------

Apache Maven 3.3.1
Java Version 1.8
IntelliJ(optional)

What is the starting point?
---------------------------

org.eega.server.HttpServerApplication is an entry point.

How to run from command line?
-----------------------------

1. open command terminal
2. execute 'mvn clean install' command (This will generate jar file in target folder)
3. execute 'java -jar target/WebServer-1.0.0.jar' or 'mvn spring-boot:run' command
4. Default port number is 9090 and directory is current directory. To change the default directory and/or port number execute below command.
    'java -jar -Dport=8050 -Dapp.rootDirectory=target/ target/WebServer-1.0.0.jar'
5. After executing the above command the socket will start listening, you will see a message
   'Listening for connection on port 9090/or user defined port':
6. Open web browser and enter URL - http://localhost:port-number/ .
    For default settings URL will be http://localhost:9090
7. Now you can navigate inside the directory from browser.

Reports:

After executing 'mvn clean install' command, following reports will be available:
    1. Unit Test report will be available in target/surefire-reports.
