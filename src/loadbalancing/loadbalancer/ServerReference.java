package loadbalancing.loadbalancer;

import loadbalancing.IServer;
import org.apache.xmlrpc.XmlRpcClient;
import java.util.Vector;

/**
 * @author Gary Ye
 */
public class ServerReference implements IServer {
    private String url;

    public ServerReference(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String call(String request) {
        // http://ws.apache.org/xmlrpc/client.html
        try {
            XmlRpcClient client = new XmlRpcClient(url);

            Vector v = new Vector();
            v.add(request);

            return (String) client.execute("Server.call", v);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

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
