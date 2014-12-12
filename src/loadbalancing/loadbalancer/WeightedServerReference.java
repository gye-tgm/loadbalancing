package loadbalancing.loadbalancer;

/**
 * Created by gary on 12/12/14.
 */
public class WeightedServerReference extends ServerReference {
    private int weight;

    public WeightedServerReference(int weight) { this.weight = weight; }

    public int getWeight() {return weight; }
}
