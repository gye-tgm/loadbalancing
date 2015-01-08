package loadbalancing;

import com.sun.xml.internal.ws.encoding.soap.DeserializationException;

/**
 * A simple serializable wrapper-class for a server request with additional header data such as the requestors name
 *
 * @author Elias Frantar
 * @version 2014-12-29
 */
public class Request {
    private final String requestor;
    private final String body;

    /**
     * Deserializes the given request-String
     * @param serialized a serialized request
     */
    public Request(String serialized) {
        String[] attrs = serialized.split(":");

        if (attrs.length != 2)
            throw new DeserializationException("Serialized data was corrupted");

        this.requestor = attrs[0];
        this.body = attrs[1];
    }

    /**
     * Creates a new request with the given parameters
     * <p><i>Note:</i> ':' are not allowed an will be stripped</p>
     *
     * @param requestor the name of the requestor
     * @param body the body of the request
     */
    public Request(String requestor, String body) {
        /* ':' are forbidden since they are used in serialized structure */
        this.requestor = requestor.replace(":", "");
        this.body = body.replace(":", "");
    }

    /**
     * Serializes the request to a String representation
     * @return the serialized request
     */
    public String serialize() {
        return requestor + ":" + body;
    }

    public String getRequestor() { return requestor; }
    public String getBody() { return body; }
}
