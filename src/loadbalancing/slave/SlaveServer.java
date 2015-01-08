package loadbalancing.slave;

import com.sun.xml.internal.ws.encoding.soap.DeserializationException;
import loadbalancing.IServer;
import loadbalancing.Request;
import org.apache.log4j.Logger;
import org.apache.xmlrpc.WebServer;

/**
 * A slave server is the end node that will actually process requests and send responses.
 *
 * @author Gary Ye
 * @author Elias Frantar
 * @version 2015-01-08
 */
public class SlaveServer extends Thread implements IServer {
    private static Logger log = Logger.getLogger(SlaveServer.class.getName());

    private int port;

    /**
     * Initializes a new slave server object with a given port number.
     * @param port the port number
     */
    public SlaveServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            WebServer webServer = new WebServer(port);
            webServer.addHandler("Server", this);
            webServer.start();
        } catch (Exception e) {
            System.out.print("Starting the server failed: " + e.getMessage());
        }
    }

    @Override
    public String call(String request) throws Exception {
        log.info("Received request: " + request);

        String content;
        try {
            content = new Request(request).getBody(); // deserialize the request to get the requestor
        } catch (DeserializationException e) {
            return "Corrupted request ...";
        }

        return "Response (" + port + "): Your request was '" + content + "'."; // we only want the request body
    }

    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println("SlaveServer with port " + arg + " started");
            new SlaveServer(Integer.parseInt(arg)).start();
        }
    }
}
