/**
 * Created by joe on 14/02/15.
 */
import java.rmi.Remote;
import java.util.HashSet;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class WhiteboardServer extends UnicastRemoteObject implements IWhiteboardServer{

    public static final String SERVER_NAME = "Whiteboard";
    private HashSet<WhiteboardClient> clients;


    public static void main(String args[]){
        try{
            WhiteboardServer server = new WhiteboardServer();
            Naming.rebind(SERVER_NAME, server);
            System.out.printf("[+] Bound to %s%n", WhiteboardServer.SERVER_NAME);

        }catch(Exception e){
            System.err.println("An error has occurred!");
            e.printStackTrace();
        }
    }

    public WhiteboardServer() throws RemoteException{
        super();
        this.clients = new HashSet<WhiteboardClient>();
    }

    public ICommunicationContext register(WhiteboardClient c) throws IllegalStateException, RemoteException{
        if(this.clients.contains(c)) {
            throw new IllegalStateException(String.format("Client %s has already been registered", c.toString()));
        }

        this.clients.add(c);
        ICommunicationContext clientContext = new CommunicationContext(c, this);
        System.out.printf("[+] Registered client %s%n", c.toString());
        return clientContext;
    }

}
