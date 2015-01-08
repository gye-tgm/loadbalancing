package loadbalancing.loadbalancer;

import com.sun.xml.internal.ws.encoding.soap.DeserializationException;
import loadbalancing.IServer;
import loadbalancing.Request;
import loadbalancing.loadbalancer.strategies.LoadBalancingStrategy;
import loadbalancing.loadbalancer.strategies.lcf.LCF;
import loadbalancing.loadbalancer.strategies.lcf.LCFServerReference;
import loadbalancing.loadbalancer.strategies.wrr.WRR;
import loadbalancing.loadbalancer.strategies.wrr.WeightedServerReference;
import org.apache.xmlrpc.WebServer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is a load balancer implementation that uses a specific load balancing strategy for balancing the traffic
 * load by assigning the given requests to different servers.
 *
 * @author Gary Ye
 * @author Elias Frantar
 * @version 2015-01-08
 */
public class LoadBalancer extends Thread implements IServer {
    
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
        sessionTable = new HashMap<>();
    }

    @Override
    public synchronized String call(String request) throws Exception {
        ServerReference serverReference;
        String requestor;

        try {
            requestor = new Request(request).getRequestor(); // deserialize the request to get the requestor
        } catch (DeserializationException e) {
            return "Corrupted request ...";
        }

        /* if there is an ongoing session use it */
        if (sessionTable.containsKey(requestor))
            serverReference = sessionTable.get(requestor);
        else
            serverReference = strategy.getNext();

        while (serverReference != null) { // try until we find a working server
            try {
                strategy.increment(serverReference);
                String result = serverReference.call(request);

                serverReference.setFailures(0); // the last call was successful
                sessionTable.put(requestor, serverReference); // register or renew current session

                return result;
            } catch (Exception e) { // if the request failed
                int failures = serverReference.getFailures();

                if (failures >= MAX_FAILUES) { // if the server failed too often remove it permanently
                    strategy.unregister(serverReference);

                    /* end all sessions associated to it */
                    for (String key : sessionTable.keySet())
                        if (sessionTable.get(key).equals(serverReference))
                            sessionTable.remove(key);
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
            loadBalancer = readXML(file);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.err.printf("Error occurred while reading the file: %s\n", args[1]);
        } catch (StrategyNotFoundException e) {
            System.err.println("Strategy not found! Choose from the following list: WRR, LCF");
        }
        loadBalancer.setPort(Integer.parseInt(args[0]));

        loadBalancer.start();
        System.out.println("Load Balancer started ...");
    }

    /**
     * Reads the xml config file and returns the corresponding load balancer (with the registered server references)
     *
     * @param xmlFile the xml config file
     * @return the loadbalancer
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws StrategyNotFoundException
     */
    private static LoadBalancer readXML(File xmlFile) throws ParserConfigurationException, IOException, SAXException,
            StrategyNotFoundException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        LoadBalancer loadBalancer = null;
        Element e = (Element) doc.getElementsByTagName("loadbalancing").item(0);
        NodeList nodeList = doc.getElementsByTagName("slave");

        String strategy = e.getElementsByTagName("strategyname").item(0).getTextContent();
        if (strategy.equals("LCF")) {
            loadBalancer = new LoadBalancer(new LCF());
        } else if (strategy.equals("WRR")) {
            loadBalancer = new LoadBalancer(new WRR());
        } else {
            throw new StrategyNotFoundException();
        }

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String url = element.getElementsByTagName("url").item(0).getTextContent();
                if (strategy.equals("LCF")) {
                    loadBalancer.register(new LCFServerReference(url));
                } else if (strategy.equals("WRR")) {
                    int weight = Integer.parseInt(element.getElementsByTagName("weight").item(0).getTextContent());
                    loadBalancer.register(new WeightedServerReference(url, weight));
                }
            }
        }

        return loadBalancer;
    }

    /* Getters */
    public void setPort(int port) { this.port = port; }

    /**
     * A simple Exception thrown when a requested strategy was not found
     */
    static class StrategyNotFoundException extends Exception {}

}
