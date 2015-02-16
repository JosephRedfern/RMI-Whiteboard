import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by joe on 14/02/15.
 */
public class CommunicationContext extends UnicastRemoteObject implements ICommunicationContext, Serializable{
    private WhiteboardClient client;
    private WhiteboardServer server;

    public CommunicationContext(WhiteboardClient client, WhiteboardServer server) throws RemoteException{
        this.client = client;
        this.server = server;
    }

    public void sendMessage(String s){
        System.out.printf("Message from %s: %s", this.client, s);
        this.client.sendMessage("Hello!");
    }
}
