package loadbalancing.loadbalancer;

import loadbalancing.IServer;
import loadbalancing.loadbalancer.strategies.LoadBalancingStrategy;
import loadbalancing.loadbalancer.strategies.lcf.LCF;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

import java.io.IOException;

/**
 * This class is a load balancer implementation that uses a specific load
 * balancing strategy for balancing the traffic load by assigning the given
 * requests to different servers.
 *
 * @author Gary Ye
 * @author Elias Frantar
 * @version 2014/12/29
 */
public class LoadBalancer extends Thread implements IServer {
    private LoadBalancingStrategy strategy;
    private int port = 5000;

    /**
     * Initializes a new LoadBalancer object with the given load balancing strategy.
     *
     * @param strategy the load balancing strategy to apply during runtime
     */
    public LoadBalancer(LoadBalancingStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public synchronized String call(String request) {
        ServerReference serverReference = strategy.getNext();
        strategy.increment(serverReference);
        return serverReference.call(request);
    }

    @Override
    public void run() {
        // http://ws.apache.org/xmlrpc/server.html
        WebServer webServer = new WebServer(port);

        XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();

        PropertyHandlerMapping phm = new PropertyHandlerMapping();
        try {
            phm.addHandler("Server", LoadBalancer.class);
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

    /**
     * Registers a new server to the load balancer by passing the reference of the server.
     *
     * @param serverReference the reference of the server that should be registered
     */
    public void register(ServerReference serverReference) {
        strategy.register(serverReference);
    }

    public static void main(String[] args){
        LoadBalancer loadBalancer = new LoadBalancer(new LCF());

        // TODO: get the slave servers from args[]
        loadBalancer.register(new ServerReference(""));

        loadBalancer.start();
    }
}
