package loadbalancing.loadbalancer;

import loadbalancing.loadbalancer.strategies.LoadBalancingStrategy;
import loadbalancing.loadbalancer.strategies.lcf.LCF;

/**
 *
 */
public class LoadBalancer {
    private LoadBalancingStrategy loadBalancingStrategy;

    public LoadBalancer(){
        loadBalancingStrategy = new LCF();
        WeightedServerReference server1 = null;
        loadBalancingStrategy.register(server1);
    }

    public static void main(String[] args){
        WeightedServerReference d = new WeightedServerReference(5);
        ServerReference e = d;
        WeightedServerReference f = (WeightedServerReference) e;
        System.out.println(f.weight);
    }
}
