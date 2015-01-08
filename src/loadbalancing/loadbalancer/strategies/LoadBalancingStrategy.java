package loadbalancing.loadbalancer.strategies;

import loadbalancing.loadbalancer.ServerReference;

/**
 * The interface which must be implemented by every load-balancing strategy.
 *
 * @author Gary Ye
 * @version 2014-12-12
 */
public interface LoadBalancingStrategy {

    /**
     * To notify that a server has succesfully established another connection
     * @param server the server to increment
     */
    public void increment(ServerReference server);

    /**
     * To notify that a server has ended another connection
     * @param server the server to decrement
     */
    public void decrement(ServerReference server);

    /**
     * Returns the next server calculated by the implemented load-balancing algorithm
     * @return the next server chosen by the algorithm
     */
    public ServerReference getNext();

    /**
     * Adds another server to the pool of available ones
     * @param server the server to add
     */
    public void register(ServerReference server);

    /**
     * Removes a server from the pool of available ones
     * @param server the server to remove
     */
    public void unregister(ServerReference server);

}
