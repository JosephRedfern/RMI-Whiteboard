/**
 * Created by joe on 14/02/15.
 */

import java.awt.*;
import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;

public class WhiteboardClient extends UnicastRemoteObject implements IWhiteboardClient, Remote{
    IWhiteboardServer server;
    ICommunicationContext context;

    private String name;

    private static final long serialVersionUID = 12351412354525L;

    public WhiteboardClient(IWhiteboardServer server) throws RemoteException, ServerNotActiveException{
        super();

        try {
            this.context = server.register(this);
            System.out.println("[+] Registered with remote sever");
            this.context.sendMessage("Hello from Client");
            this.context.addShape(new Rectangle(10, 10, 10, 10));

        }catch(RemoteException e){
            System.err.println("[!] Error registering with remote server");
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        System.out.printf("Message from Server: %s%n", message);
    }

    public ICommunicationContext getContext(){
        return this.context;
    }

    public void setName(String name){
        this.name = name;
    }

    public static void main(String args[]) throws Exception //TODO: specific exception handling
    {
        String serverAddress = "//127.0.0.1/Whiteboard";

        if(args.length > 1){
            serverAddress = args[0];
        }

        IWhiteboardServer server = (IWhiteboardServer)Naming.lookup(serverAddress);
        System.out.printf("Server object hash (client-side): %s%n", server.hashCode());
        IWhiteboardClient client = new WhiteboardClient(server);
        System.out.printf("[+] Acquired session: %s%n", server);
        WhiteboardClientGUI ui = new WhiteboardClientGUI(client);

    }

    @Override
    public String getName(){
        if(this.name != null){
            return String.format("[WhiteboardClient: \"%s\"]", this.name);
        }else{
            return String.format("[WhiteboardClient: %d]", this.hashCode());
        }
    }

    public void retrieveShape(IWhiteboardItem shape) throws RemoteException{
        System.out.printf("[+] Retrieved shape from " + shape.getOwner());
    }

    public double pingClient(){
        return System.currentTimeMillis();
    }

}
