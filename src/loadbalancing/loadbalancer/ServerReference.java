package loadbalancing.loadbalancer;

import loadbalancing.IServer;
import org.apache.xmlrpc.XmlRpcClient;
import java.util.Vector;

/**
 * A reference to a server implementation<br>
 * It provides utility for sending real requests to the referenced server
 *
 * <p><i>The load-balancer uses this references.</i></p>
 *
 * @author Gary Ye
 * @version 2015-01-08
 */
public class ServerReference implements IServer {
    private String url;
    private int failures;

    /**
     * Creates a new ServerReference with the given parameters
     * @param url the url of the server to reference to
     */
    public ServerReference(String url) {
        this.url = url;
        this.failures = 0;
    }

    @Override
    public String call(String request) throws Exception {
        XmlRpcClient client = new XmlRpcClient(url);

        Vector v = new Vector();
        v.add(request);

        return (String) client.execute("Server.call", v);
    }

    /* `equals()` and `hashCode()` are necessary for proper comparison */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServerReference that = (ServerReference) o;

        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

    /* Getters and Setters */

    public String getUrl() { return url; }
    public int getFailures() { return failures; }
    public void setFailures(int failures) { this.failures = failures; }
}
