package loadbalancing.client;

import loadbalancing.Request;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

/**
 * This class represents the client who will make requests to the
 * load balancer.
 *
 * @author Gary Ye
 */
public class Client {
    private String name;
    private String url;

    public Client(String name, String url) {
        this.name = name;
        this.url = url;
    }

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
