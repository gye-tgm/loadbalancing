package loadbalancing.loadbalancer.strategies;

import loadbalancing.loadbalancer.ServerReference;

/**
 * A simple dummy-load-balancer that automatically increments the servers after calls to {@link #getNext()}.<br>
 * <b>Only meant for testing purposes!</b>
 *
 * @author Elias Frantar
 * @version 2015-01-08
 */
public class TestLoadBalancer implements LoadBalancingStrategy {
    private LoadBalancingStrategy strategy;

    public TestLoadBalancer(LoadBalancingStrategy strategy) {
        this.strategy = strategy;
    }

    public void increment(ServerReference server) { strategy.increment(server); }
    public void decrement(ServerReference server) { strategy.decrement(server); }

    public ServerReference getNext() {
        ServerReference next = strategy.getNext();
        strategy.increment(next); // increment connections
        return next;
    }

    public void register(ServerReference server) { strategy.register(server); }
    public void unregister(ServerReference server) { strategy.unregister(server); }
}
