package loadbalancing.loadbalancer.strategies.lcf;

import loadbalancing.loadbalancer.ServerReference;

/**
 * @author Gary Ye
 */
public class LCFServerReference extends ServerReference {
    private int connections;


    public LCFServerReference(String url) {
        super(url);
        this.connections = 0;
    }

    public int getConnections() {
        return connections;
    }

    public void setConnections(int connections) {
        this.connections = connections;
    }

    public void increment() {
        setConnections(getConnections() + 1);
    }

    public void decrement() {
        setConnections(getConnections() - 1);
    }
}
