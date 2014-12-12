package loadbalancing.loadbalancer;

/**
 * Created by gary on 12/12/14.
 */
public class WeightedServerReference extends ServerReference {
    int weight;

    public WeightedServerReference(int weight) {
        this.weight = weight;
    }
}
