package loadbalancing.loadbalancer.strategies.lcf;

import loadbalancing.loadbalancer.ServerReference;
import loadbalancing.loadbalancer.strategies.wrr.WeightedServerReference;

/**
 * @author Gary Ye
 */
public class LCFServerReference extends ServerReference {
    private int connections;

    public LCFServerReference(String url, int connections) {
        super(url);
        this.connections = connections;
    }

    public int getConnections() {
        return connections;
    }
}
