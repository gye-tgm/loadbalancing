package loadbalancing.loadbalancer.strategies.wrr;

import loadbalancing.loadbalancer.ServerReference;

/**
 *
 */
public class WeightedServerReference extends ServerReference {
    private int weight;

    public WeightedServerReference(String url, int weight) {
        super(url);
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
}
