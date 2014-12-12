package loadbalancing.slave;

import loadbalancing.Server;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.webserver.WebServer;

import java.io.IOException;

/**
 * @author Gary Ye
 */
public class SlaveServer extends Thread implements Server {
    private int port;

    public SlaveServer() {
    }

    public SlaveServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        System.out.println("Attempting to start XML-RPC Server...");
        WebServer webServer = new WebServer(port);

        PropertyHandlerMapping mapping = new PropertyHandlerMapping();
        try {
            mapping.addHandler("lb", new SlaveServer().getClass());
            XmlRpcServer server = webServer.getXmlRpcServer();
            server.setHandlerMapping(mapping);
            webServer.start();
        } catch (XmlRpcException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String call(String request) {
        return "You requested with " + request + " I am with port " + port;
    }

    public static void main(String[] args) {
        // Args: Port numbers :-)
        for (String arg : args) new SlaveServer(Integer.parseInt(arg)).start();
    }
}
