package loadbalancing.loadbalancer.strategies.lcf;

import loadbalancing.loadbalancer.WeightedServerReference;

/**
 * Created by gary on 12/12/14.
 */
public class LCFServerReference extends WeightedServerReference {
    public LCFServerReference(int a) {
        super(a);
    }
}
