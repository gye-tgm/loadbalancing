package loadbalancing;

/**
 * The base interface that must be implemented by every server
 *
 * @author Gary Ye
 * @version 2014-12-10
 */
public interface IServer {

    /**
     * Processes the request and returns a response
     *
     * @param request the request to process
     * @return the response to the received request
     * @throws Exception thrown if something went wrong
     */
    public String call(String request) throws Exception;

}
