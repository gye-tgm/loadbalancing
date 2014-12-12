package loadbalancing.loadbalancer.strategies;

import loadbalancing.loadbalancer.ServerReference;

/**
 * @author Gary Ye
 * @version 2014/12/12
 */
public interface LoadBalancingStrategy {
    public void increment(ServerReference server);
    public void decrement(ServerReference server);
    public ServerReference getNext();
    public void register(ServerReference server);
    public void unregister(ServerReference server);
}
