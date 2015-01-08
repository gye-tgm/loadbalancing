package loadbalancing.loadbalancer.strategies.wrr;

import loadbalancing.loadbalancer.ServerReference;

/**
 * A server reference for the  <i>Weighted Round Robin</i> algorithm (with an additional attribute <i>weight</i>)
 *
 * @author Elias Frantar
 * @version 2014-15-12
 */
public class WeightedServerReference extends ServerReference {
    private int weight;

    /**
     * Creates a new WeightedServerReference with the given parameters
     * @param url the url to reference to
     * @param weight the weight of the reference (essential for the algorithm)
     */
    public WeightedServerReference(String url, int weight) {
        super(url);
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
}
