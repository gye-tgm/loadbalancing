package loadbalancing.slave;

import loadbalancing.IServer;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

import java.io.IOException;

/**
 * A slave server is the end node that will actually process the request.
 *
 * @author Gary Ye
 */
public class SlaveServer extends Thread implements IServer {
    private int port;

    public SlaveServer() {
        port = 100;
    }
    /**
     * Initializes a new slave server object with a given port number.
     *
     * @param port the port number
     */
    public SlaveServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        // http://ws.apache.org/xmlrpc/server.html
        WebServer webServer = new WebServer(port);

        XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();

        PropertyHandlerMapping phm = new PropertyHandlerMapping();
        try {
            phm.addHandler("Server", SlaveServer.class);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }

        xmlRpcServer.setHandlerMapping(phm);

        XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
        serverConfig.setEnabledForExtensions(true);
        serverConfig.setContentLengthOptional(false);

        try {
            webServer.start();
        } catch (IOException e) {
            System.out.println("WebServer could not be started!");
        }
    }

    @Override
    public String call(String request) {
        return "You requested with " + request + " I am with port " + port;
    }

    public static void main(String[] args) {
        for (String arg : args) new SlaveServer(Integer.parseInt(arg)).start();
    }
}
