import java.awt.*;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by joe on 14/02/15.
 */
public class CommunicationContext extends UnicastRemoteObject implements ICommunicationContext, Serializable{
    private IWhiteboardClient client;
    private IWhiteboardServer server;

    public CommunicationContext(IWhiteboardClient client, WhiteboardServer server) throws RemoteException{
        this.client = client;
        this.server = server;
    }

    public void sendMessage(String s) throws RemoteException{
        System.out.printf("Message from %s: %s", this.client, s);
        this.client.sendMessage("Hello!");
    }

    public void addShape(Shape shape) throws RemoteException{
        server.addShape(client, shape);
    }

    public ArrayList<IWhiteboardItem> getShapes() throws RemoteException{
        return server.getShapes();
    }

}
