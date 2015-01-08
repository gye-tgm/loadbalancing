package loadbalancing.loadbalancer.strategies.lcf;

import loadbalancing.loadbalancer.ServerReference;
import loadbalancing.loadbalancer.strategies.LoadBalancingStrategy;

import java.util.ArrayList;

/**
 * A <i>Least Connection First</i> implementation of a load balancing algorithm
 *
 * @author Gary Ye
 * @version 2014-13-12
 */
public class LCF implements LoadBalancingStrategy {
    ArrayList<LCFServerReference> list;

    /**
     * Creates a new <i>Least Connection First</i> load balancing strategy
     */
    public LCF() {
        list = new ArrayList<>();
    }

    @Override
    public void increment(ServerReference server) {
        int i = find(server);
        if (i != -1)
            list.get(i).increment();
    }

    @Override
    public void decrement(ServerReference server) {
        int i = find(server);
        if (i != -1)
            list.get(i).decrement();
    }

    @Override
    public ServerReference getNext() {
        if (list.isEmpty())
            return null;
        int p = 0;
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).getConnections() < list.get(p).getConnections()) {
                p = i;
            }
        }
        return list.get(p);
    }

    @Override
    public void register(ServerReference server) {
        list.add((LCFServerReference) server);
    }

    private int find(ServerReference serverReference) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(serverReference)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void unregister(ServerReference server) {
        int i = find(server);
        if (i != -1)
            list.remove(i);
    }
}
