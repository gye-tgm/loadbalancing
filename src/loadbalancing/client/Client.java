package loadbalancing.client;

/**
 * This class represents the client who will make requests to the
 * load balancer.
 *
 * @author Gary Ye
 */
public class Client {
    public static void main(String[] args){
        if (args.length != 2) {
            System.err.println("usage: java client.Client <host name> <request>");
            System.exit(1);
        }

        /*
        try {
            String hostName = args[0];
            String request = args[1];

        } catch (RemoteException e) {
            System.err.println("Connection could not established!");
        } catch (NotBoundException e) {
            System.err.println("Service is not bound");
        }
        */
    }
}
