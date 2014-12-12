package loadbalancing.loadbalancer;

import loadbalancing.Server;
import org.apache.xmlrpc.*;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gary Ye
 */
public class ServerReference implements Server {
    private String url;

    public ServerReference(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String call(String request) {
        XmlRpcClient server = new XmlRpcClient();
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        try {
            // config.setServerURL(new URL("http://localhost:12345/RPC2"));
            config.setServerURL(new URL(url));
            server.setConfig(config);
            return (String) server.execute("lb.call", new Object[]{request});
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
