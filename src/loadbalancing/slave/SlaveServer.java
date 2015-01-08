package loadbalancing.slave;

import loadbalancing.IServer;
import org.apache.xmlrpc.WebServer;

/**
 * A slave server is the end node that will actually process the request.
 *
 * @author Gary Ye
 * @author Elias Frantar
 * @version 2015-01-08
 */
public class SlaveServer extends Thread implements IServer {
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
        return "Response (" + port + "): Your request was '" + request + "'.";
    }

    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println("SlaveServer with port + " + arg + " started\n");
            new SlaveServer(Integer.parseInt(arg)).start();
        }
    }
}
