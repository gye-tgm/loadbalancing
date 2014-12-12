package loadbalancing.loadbalancer;

import loadbalancing.Server;
import loadbalancing.loadbalancer.strategies.LoadBalancingStrategy;
import loadbalancing.loadbalancer.strategies.lcf.LCF;
import loadbalancing.loadbalancer.strategies.lcf.LCFServerReference;
import loadbalancing.loadbalancer.strategies.wrr.WeightedServerReference;

/**
 * @author Gary Ye
 * @author Elias Frantar
 * @version 2014/12/12
 *
 */
public class LoadBalancer extends Thread implements Server {
    private LoadBalancingStrategy strategy;

    public LoadBalancer(LoadBalancingStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void run() {
        while (true) {
            // TODO: Waiting for requests ...
        }
    }

    @Override
    public String call(String request) {
        return strategy.getNext().call(request);
    }

    public void register(ServerReference serverReference) {
        strategy.register(serverReference);
    }

    public static void main(String[] args){
        LoadBalancer loadBalancer = new LoadBalancer(new LCF());
        loadBalancer.register(new WeightedServerReference("localhost:500", 5));
        loadBalancer.register(new LCFServerReference("localhost:501", 6));
        loadBalancer.start();
    }
}
