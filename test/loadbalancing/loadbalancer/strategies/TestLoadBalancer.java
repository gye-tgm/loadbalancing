package loadbalancing.loadbalancer.strategies;

import loadbalancing.loadbalancer.ServerReference;

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
