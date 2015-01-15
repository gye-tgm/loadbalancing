package loadbalancing.loadbalancer;

import loadbalancing.loadbalancer.strategies.lcf.LCF;
import loadbalancing.loadbalancer.strategies.lcf.LCFServerReference;
import loadbalancing.loadbalancer.strategies.wrr.WRR;
import loadbalancing.loadbalancer.strategies.wrr.WeightedServerReference;
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
 * This class can read the configuration of a load balancer from an XML file,
 * and return it.
 *
 * @author Gary Ye
 * @version 2014-01-08
 */
public class LoadBalancerConfigXMLReader {
    /**
     * Reads the xml config file and returns the corresponding load balancer (with the registered server references)
     *
     * @param xmlFile the xml config file
     * @return the loadbalancer
     * @throws javax.xml.parsers.ParserConfigurationException thrown if the parser configuration was invalid
     * @throws java.io.IOException thrown if an IOException occurred
     * @throws org.xml.sax.SAXException thrown if a SAXException occurred
     * @throws StrategyNotFoundException throw if the specified strategy was not found
     */
    public LoadBalancer readXML(File xmlFile) throws ParserConfigurationException, IOException, SAXException,
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

    /**
     * A simple Exception thrown when a requested strategy was not found
     */
    class StrategyNotFoundException extends Exception {
    }
}

