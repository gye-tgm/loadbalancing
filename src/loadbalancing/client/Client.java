package loadbalancing.client;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class represents the client who will make requests to the
 * load balancer.
 *
 * @author Gary Ye
 */
public class Client {
    private String url;

    public Client(String url) {
        this.url = url;
    }

    public String call(String request) {
        // http://ws.apache.org/xmlrpc/client.html
        try {
            XmlRpcClient client = new XmlRpcClient();
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL(url));
            client.setConfig(config);
            return (String) client.execute("Server.call", new Object[]{request});
        } catch (MalformedURLException | XmlRpcException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("usage: java client.Client <url> <request>");
            System.exit(1);
        }

        System.out.println(new Client(args[0]).call(args[1]));
    }
}
