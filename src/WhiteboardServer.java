/**
 * Created by joe on 14/02/15.
 */

import java.io.Serializable;
import java.util.HashSet;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class WhiteboardServer extends UnicastRemoteObject implements IWhiteboardServer, Serializable{
    private HashSet<IWhiteboardClient> clients;

    public WhiteboardServer() throws RemoteException{
        super();
        this.clients = new HashSet<IWhiteboardClient>();
    }

    public ICommunicationContext register(IWhiteboardClient c) throws IllegalStateException, RemoteException{
        if(this.clients.contains(c))
            throw new IllegalStateException(String.format("Client %s has already been registered", c.toString()));

        this.clients.add(c);
        ICommunicationContext clientContext = new CommunicationContext(c, this);
        System.out.printf("[+] Registered client %s%n", c.toString());

        return clientContext;
    }
}
