package loadbalancing.loadbalancer.strategies.wrr;

import loadbalancing.Server;
import loadbalancing.loadbalancer.ServerReference;
import loadbalancing.loadbalancer.strategies.LoadBalancingStrategy;

import java.util.ArrayList;

/**
 * @author Elias Frantar
 */
public class WRR implements LoadBalancingStrategy {

    private class WRREntry {
        Server server;
        int weight;
        int assigned;

        public WRREntry(Server server, int weight) {
            this.server = server;
            this.weight = weight;
            this.assigned = 0;
        }
    }

    private ArrayList<WRREntry> list;
    private int current;

    public WRR() {
        list = new ArrayList<>();
        current = 0;
    }


    public void register(Server server, int weight) {
        list.add(new WRREntry(server, weight));
    }

    @Override
    public void increment(ServerReference server) {

    }

    @Override
    public void decrement(ServerReference server) {

    }

    @Override
    public ServerReference getNext() {
        return null;
    }

    @Override
    public void register(ServerReference server) {
        WeightedServerReference wrrserver = (WeightedServerReference) server;
        list.add(new WRREntry(wrrserver, wrrserver.getWeight()));
    }

    @Override
    public void unregister(ServerReference server) {
        list.remove(server);
    }

}
