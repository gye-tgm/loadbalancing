package loadbalancing.loadbalancer;

import loadbalancing.IServer;
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

    public static void usage() {
        System.out.println("usage: java loadbalancing.loadbalancer.LoadBalancer <config-file>");
    }

    public static void main(String[] args){
        if (args.length != 1) {
            usage();
            return;
        }
        LoadBalancer loadBalancer = null;
        try {
            File file = new File(args[0]);
            loadBalancer = readXML(file);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.err.printf("Error occurred while reading the file: %s\n", args[0]);
        } catch (StrategyNotFoundException e) {
            System.err.println("Strategy not found! Choose from the following list: WRR, LCF");
        }
        loadBalancer.start();
        System.out.println("Load Balancer started...\n");
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

    static class StrategyNotFoundException extends Exception {
    }
}
