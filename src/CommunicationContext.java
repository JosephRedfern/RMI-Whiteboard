import java.awt.*;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * This class is used by the client to talk to the server.
 *
 * We COULD use a WhiteboardServer object directly, but we'd need to pass 'this' to every method call.
 *
 * By creating a wrapper around WhiteboardServer, and passing in the client to the constructor, we can avoid all the
 * passing around of 'this'.
 *
 *
 * Created by joe on 14/02/15.
 */
public class CommunicationContext extends UnicastRemoteObject implements ICommunicationContext, Serializable{
    private IWhiteboardClient client;
    private IWhiteboardServer server;

    public CommunicationContext(IWhiteboardClient client, WhiteboardServer server) throws RemoteException{
        this.client = client;
        this.server = server;
    }

    /***
     * Sends an arbitrary message to the server. Primarily used for testing
     * @param s - the text to send.
     * @throws RemoteException
     */
    public void sendMessage(String s) throws RemoteException{
        System.out.printf("Message from %s: %s", this.client, s);
        this.client.sendMessage("Hello!");
    }

    public void addShape(Shape shape, Color colour) throws RemoteException{
        server.addShape(client, shape, colour);
    }

    /***
     * Retrieve all IWhiteboardItem's from the server.
     * @return List of all current shapes.
     * @throws RemoteException
     */
    public ArrayList<IWhiteboardItem> getShapes() throws RemoteException{
        return server.getShapes();
    }

    /***
     * Tells the server to remove all IWhiteboardItem's owned by this client.
     * @throws RemoteException
     */
    public void clearShapes() throws RemoteException{
        server.removeItemsByClient(client);
    }

}
