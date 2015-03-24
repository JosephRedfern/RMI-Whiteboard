/**
 * Created by joe on 14/02/15.
 */

import java.awt.*;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class WhiteboardClient extends UnicastRemoteObject implements IWhiteboardClient, Remote{
    private IWhiteboardServer server;
    private ICommunicationContext context;
    private String name;
    private ArrayList<IWhiteboardItemListener> listeners;

    private static final long serialVersionUID = 12351412354525L;

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

    /***
     * Create a new instance of a WhiteboardClient. The WhiteboardClient is a middle-man between the WhiteboardClientGUI
     * and the WhiteboardServer.
     *
     * @param server
     * @throws RemoteException
     * @throws ServerNotActiveException
     */
    public WhiteboardClient(IWhiteboardServer server) throws RemoteException, ServerNotActiveException{
        super();

        this.listeners = new ArrayList<IWhiteboardItemListener>();
        try {
            this.context = server.register(this);
            System.out.println("[+] Registered with remote sever");
            this.context.sendMessage("Hello from Client");

        }catch(RemoteException e){
            System.err.println("[!] Error registering with remote server");
            e.printStackTrace();
        }
    }

    /***
     * Recieve a message..
     * @param message
     */
    public void sendMessage(String message){
        System.out.printf("Message from Server: %s%n", message);
    }

    /***
     * Gets the ICommunicationContext associated with this WhiteboardClient
     * @return
     */
    public ICommunicationContext getContext(){
        return this.context;
    }

    /***
     * Sets the client name.
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }

    /***
     * A friendly-ish name for the client, when client name is set.
     * @return friendly-ish name
     */
    @Override
    public String getName(){
        if(this.name != null){
            return String.format("[WhiteboardClient: \"%s\"]", this.name);
        }else{
            return String.format("[WhiteboardClient: %d]", this.hashCode());
        }
    }

    /***
     * Listeners get the shapes passed to the WhiteboardClient by a WhiteboardServer from a callback.
     * @param listener
     */
    public void addItemListener(IWhiteboardItemListener listener){
        this.listeners.add(listener);
    }

    //Dispatches shapes to Whiteboard Item Listeners (typically GUI).
    public void retrieveShape(IWhiteboardItem shape) throws RemoteException{
        for(IWhiteboardItemListener listener : this.listeners){
            listener.receiveShape(shape);
        }
    }

    /***
     * Tells all IWhoteboardItemListeners to resync their shapes.
     * @throws RemoteException
     */
    public void resync() throws RemoteException{
        for(IWhiteboardItemListener listener : this.listeners){
            listener.resync();
        }
    }

    /***
     * Just returns /something/, used to check if the client is online.
     * @return
     */
    public double pingClient(){
        return System.currentTimeMillis();
    }

}
