package loadbalancing.loadbalancer.strategies.lcf;

import loadbalancing.loadbalancer.ServerReference;

/**
 * A server reference for the  <i>Least Connection First</i> algorithm (with an additional attribute <i>connections</i>)
 *
 * @author Gary
 * @version 2014-15-12
 */
public class LCFServerReference extends ServerReference {
    private int connections;

    /**
     * Creates a new LCFServerReference with the given parameters
     * @param url the url to reference to
     */
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
