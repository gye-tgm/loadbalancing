package loadbalancing.loadbalancer;

import com.sun.xml.internal.ws.encoding.soap.DeserializationException;
import loadbalancing.IServer;
import loadbalancing.Request;
import loadbalancing.loadbalancer.strategies.LoadBalancingStrategy;
import org.apache.log4j.Logger;
import org.apache.xmlrpc.WebServer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is a load balancer implementation that uses a specific load balancing strategy for balancing the traffic
 * load by assigning the given requests to different servers.
 *
 * @author Gary Ye
 * @author Elias Frantar
 * @version 2015-01-09
 */
public class LoadBalancer extends Thread implements IServer {

    private static Logger log = Logger.getLogger(LoadBalancer.class.getName());

    private static final int MAX_FAILUES = 2;
    private static final int DEFAULT_PORT = 5000;

    private LoadBalancingStrategy strategy;
    private Map<String, ServerReference> sessionTable;
    private int port = 5000;

    /**
     * Initializes a new LoadBalancer object with the given load balancing strategy
     * @param strategy the load balancing strategy to apply during runtime
     */
    public LoadBalancer(LoadBalancingStrategy strategy) {
        this(strategy, DEFAULT_PORT);
    }

    /**
     *Initializes a new LoadBalancer object with the given load balancing strategy and port
     * @param strategy the load balancing strategy to apply during runtime
     * @param port the port to start the load-balancer on
     */
    public LoadBalancer(LoadBalancingStrategy strategy, int port) {
        this.strategy = strategy;
        this.port = port;
        sessionTable = new ConcurrentHashMap<>(); // must be thread-safe
    }

    @Override
    public String call(String request) throws Exception {
        ServerReference serverReference;
        String requestor;

        try {
            requestor = new Request(request).getRequestor(); // deserialize the request to get the requestor
        } catch (DeserializationException e) {
            return "Corrupted request ...";
        }

        /* if there is an ongoing session use it */
        if (sessionTable.containsKey(requestor)) {
            log.info("There is an ongoing session for: " + requestor);
            serverReference = sessionTable.get(requestor);
        }
        else
            serverReference = strategy.getNext();

        while (serverReference != null) { // try until we find a working server
            try {
                log.debug("Forwarding request to: " + serverReference.getUrl());

                strategy.increment(serverReference); // signalize that a new connection has started
                String result = serverReference.call(request);
                strategy.decrement(serverReference); // notify that the connection has ended again

                serverReference.setFailures(0); // the last call was successful
                sessionTable.put(requestor, serverReference); // register or renew current session

                return result;
            } catch (Exception e) { // if the request failed
                log.info("Forwarding request failed ... Trying again ...");

                int failures = serverReference.getFailures();

                if (failures >= MAX_FAILUES) { // if the server failed too often remove it permanently
                    strategy.unregister(serverReference);

                    /* end all sessions associated to it */
                    for (String key : sessionTable.keySet())
                        if (sessionTable.get(key).equals(serverReference))
                            sessionTable.remove(key);

                    log.info("Unregistered server: " + serverReference.getUrl());
                }
                else
                    serverReference.setFailures(failures + 1);
            }

            serverReference = strategy.getNext(); // try the next server
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
     * @param serverReference the reference of the server that should be registered
     */
    public void register(ServerReference serverReference) {
        strategy.register(serverReference);
        log.info("Registered new server: " + serverReference.getUrl());
    }

    /**
     * Prints the usage of this class' {@link #main(String[])} method
     */
    public static void usage() {
        System.out.println("usage: java loadbalancing.loadbalancer.LoadBalancer <port> <config-file>");
    }

    public static void main(String[] args){
        if (args.length != 2) {
            usage();
            return;
        }

        LoadBalancer loadBalancer = null;
        try {
            File file = new File(args[1]);
            loadBalancer = new LoadBalancerConfigXMLReader().readXML(file);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.err.printf("Error occurred while reading the file: %s\n", args[1]);
        } catch (StrategyNotFoundException e) {
            System.err.println("Strategy not found! Choose from the following list: WRR, LCF");
        }
        loadBalancer.setPort(Integer.parseInt(args[0]));

        loadBalancer.start();
        System.out.println("Load Balancer started ...");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoadBalancer that = (LoadBalancer) o;

        if (port != that.port) return false;
        if (sessionTable != null ? !sessionTable.equals(that.sessionTable) : that.sessionTable != null) return false;
        if (!strategy.getClass().equals(that.strategy.getClass())) return false;
        return true;
    }
    
    /* Getters */
    public void setPort(int port) { this.port = port; }

}
