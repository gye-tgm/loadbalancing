package loadbalancing.loadbalancer;

import loadbalancing.IServer;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;

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
