package loadbalancing.client;

import loadbalancing.Request;
import org.apache.xmlrpc.XmlRpcClient;

import java.util.Vector;

/**
 * This class represents the client who will make requests to the load balancer.
 *
 * @author Gary Ye
 * @author Elias Frantar
 * @version 2015-01-08
 */
public class Client {
    private String name;
    private String url;

    /**
     * Creates a new client with the specified parameters
     * @param name the name of the client
     * @param url the url to request to
     */
    public Client(String name, String url) {
        this.name = name;
        this.url = url;
    }

    /**
     * Sends a request to the previously configured URL
     *
     * @param request the request to send
     * @return the response to this request
     * @throws Exception thrown if requesting failed
     */
    public String call(String request) throws Exception {
        XmlRpcClient client = new XmlRpcClient(url);

        Vector v = new Vector();
        v.add(new Request(name, request).serialize()); // we want to send structured requests

        return (String) client.execute("Server.call", v);
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("usage: java client.Client <name> <url> <request>");
            System.exit(1);
        }

        try {
            System.out.println(new Client(args[0], args[1]).call(args[2]));
        } catch (Exception e) {
            System.out.println("Connecting to the server failed: " + e.getMessage());
        }
    }
}
