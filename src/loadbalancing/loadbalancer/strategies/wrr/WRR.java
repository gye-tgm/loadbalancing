package loadbalancing.loadbalancer.strategies.wrr;

import loadbalancing.loadbalancer.ServerReference;
import loadbalancing.loadbalancer.strategies.LoadBalancingStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a <i>Weighted Round Robing</i> implementation of a load balancing algorithm.
 *
 * @author Elias Frantar
 * @version 2014-15-12
 */
public class WRR implements LoadBalancingStrategy {

    private ArrayList<WeightedServerReference> list;
    private Map<String, Integer> assigned;
    private int current;

    public WRR() {
        list = new ArrayList<>();
        assigned = new HashMap<>();
        current = -1;
    }

    @Override
    public synchronized ServerReference getNext() {
        WeightedServerReference server;

        // if all servers were assigned with connections according to their weight, flush the `assigned` map
        if (allFull())
            clearAssigned();

        /* get the next server with remaining connections */
        do {
            current = (current + 1) % list.size();
            server = list.get(current);
        } while (assigned.get(server.getUrl()) >= server.getWeight());

        increment(server);
        return server;
    }

    public synchronized void register(ServerReference server)   {
        list.add((WeightedServerReference) server);
        assigned.put(server.getUrl(), 0);
    }
    public synchronized void unregister(ServerReference server) {
        list.remove((WeightedServerReference) server);
        assigned.remove(server.getUrl());

        /* reset pointer if it is now larger than the list */
        if (current >= list.size())
            current = -1;
    }

    public synchronized void increment(ServerReference server) { assigned.put(server.getUrl(), assigned.get(server.getUrl())+1); }
    public synchronized void decrement(ServerReference server) { assigned.put(server.getUrl(), assigned.get(server.getUrl())-1); }

    private void clearAssigned() {
        for (String key : assigned.keySet())
            assigned.put(key, 0);
    }

    private boolean allFull() {
        for (WeightedServerReference server : list)
            if (assigned.get(server.getUrl()) < server.getWeight())
                return false;
        return true;
    }

}
