package loadbalancing.loadbalancer.strategies.lcf;

import loadbalancing.loadbalancer.ServerReference;
import loadbalancing.loadbalancer.strategies.LoadBalancingStrategy;

/**
 *
 */
public class LCF implements LoadBalancingStrategy {
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

    }

    @Override
    public void unregister(ServerReference server) {

    }
}
