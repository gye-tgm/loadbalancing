package loadbalancing.loadbalancer;

import loadbalancing.IServer;
import org.apache.xmlrpc.XmlRpcClient;
import java.util.Vector;

/**
 * @author Gary Ye
 */
public class ServerReference implements IServer {
    private String url;
    private int failures;

    public ServerReference(String url) {
        this.url = url;
        this.failures = 0;
    }

    public String getUrl() {
        return url;
    }
    public int getFailures() { return failures; }
    public void setFailures(int failures) { this.failures = failures; }

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

}
