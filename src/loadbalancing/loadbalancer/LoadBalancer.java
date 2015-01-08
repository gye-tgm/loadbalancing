package loadbalancing.loadbalancer;

import loadbalancing.IServer;
import loadbalancing.loadbalancer.strategies.LoadBalancingStrategy;
import loadbalancing.loadbalancer.strategies.lcf.LCF;
import loadbalancing.loadbalancer.strategies.lcf.LCFServerReference;
import org.apache.xmlrpc.WebServer;
import org.apache.xmlrpc.XmlRpcException;

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
    private static final int MAX_FAILUES = 2;

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
    public synchronized String call(String request) throws Exception {
        ServerReference serverReference;

        while ((serverReference = strategy.getNext()) != null) { // try as long as we find a working server
            try {
                strategy.increment(serverReference);
                String result = serverReference.call(request);

                serverReference.setFailures(0); // the last call was successful
                return result;
            } catch (Exception e) { // if the request failed
                int failures = serverReference.getFailures();

                if (failures >= MAX_FAILUES) // if the server failed too often remove it permanently
                    strategy.unregister(serverReference);
                else
                    serverReference.setFailures(failures + 1);
            }
        }

        return "Request could not be processed :( All servers are down ..."; // error if there are no servers available
    }

    @Override
    public void run() {
        WebServer webServer = new WebServer(port);
        webServer.addHandler("Server", this);
        webServer.start();
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
        loadBalancer.register(new LCFServerReference("http://localhost:5050/xmlrpc"));

        loadBalancer.start();
    }
}
