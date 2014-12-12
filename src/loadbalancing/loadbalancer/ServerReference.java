package loadbalancing.loadbalancer;

import loadbalancing.Server;

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
        // TODO: Use XML RPC for requesting
        return null;
    }
}
