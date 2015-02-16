/**
 * Created by joe on 14/02/15.
 */

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class WhiteboardClient extends UnicastRemoteObject implements IWhiteboardClient, Remote{
    IWhiteboardServer server;
    ICommunicationContext context;

    private static final long serialVersionUID = 12351412354525L;

    public WhiteboardClient(IWhiteboardServer server) throws RemoteException{
        super();

        try {
            this.context = server.register(this);
            System.out.println("[+] Registered with remote sever");
            this.context.sendMessage("Hello from Client");
        }catch(RemoteException e){
            System.err.println("[!] Error registering with remote server");
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        System.out.printf("Message from Server: %s%n", message);
    }

    public static void main(String args[]) throws Exception //TODO: specific exception handling
    {
        String serverAddress = "//127.0.0.1/Whiteboard";

        if(args.length > 1){
            serverAddress = args[0];
        }

        IWhiteboardServer server = (IWhiteboardServer)Naming.lookup(serverAddress);
        System.out.printf("Server object hash (client-side): %s%n", server.hashCode());
        System.out.println(server.getClass());
           IWhiteboardClient client = new WhiteboardClient(server);

        System.out.printf("[+] Acquired session: %s%n", server);

    }
}
