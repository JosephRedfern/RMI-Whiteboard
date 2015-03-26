/**
 * Created by joe on 14/02/15.
 */

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class WhiteboardClient extends UnicastRemoteObject implements IWhiteboardClient, Remote{
    private IWhiteboardServer server;
    private ICommunicationContext context;
    private String name;
    private ArrayList<IWhiteboardItemListener> itemListeners;
    private ArrayList<IWhiteboardClientListener> clientListeners;

    private static final long serialVersionUID = 12351412354525L;

    public static void main(String args[]) throws RemoteException, MalformedURLException, NotBoundException, ServerNotActiveException {
        String serverAddress = "//127.0.0.1/Whiteboard";

        if(args.length > 0){
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

        this.itemListeners = new ArrayList<IWhiteboardItemListener>();
        this.clientListeners = new ArrayList<IWhiteboardClientListener>();

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
        String oldName = this.name;
        this.name = name;
        try {
            this.getContext().notifyOfClientNameChange();
        }catch(RemoteException err){
            System.err.println("[!] Name Change failed, remote exception occured.");
            this.name = oldName; //try and prevent out-of-sync.
        }
    }

    /***
     * A friendly-ish name for the client, when client name is set.
     * @return friendly-ish name
     */
    @Override
    public String getName(){
        if(this.name != null){
            return this.name;
        }else{
            return String.format("[WhiteboardClient: %d]", this.hashCode());
        }
    }

    /***
     * Listeners get the shapes passed to the WhiteboardClient by a WhiteboardServer from a callback.
     * @param listener
     */
    public void addItemListener(IWhiteboardItemListener listener) {
        this.itemListeners.add(listener);
    }

    @Override
    public void addClientListener(IWhiteboardClientListener listener) throws RemoteException {
        this.clientListeners.add(listener);
    }

    //Dispatches shapes to Whiteboard Item Listeners (typically GUI).
    public void retrieveShape(IWhiteboardItem shape) throws RemoteException{
        for(IWhiteboardItemListener listener : this.itemListeners){
            listener.receiveShape(shape);
        }
    }

    /***
     * Tells all IWhoteboardItemListeners to resync their shapes.
     * @throws RemoteException
     */
    public void resyncShapes() throws RemoteException{
        for(IWhiteboardItemListener listener : this.itemListeners){
            listener.resyncShapes();
        }
    }

    public void resyncClientNameList() throws RemoteException{
        for(IWhiteboardClientListener listener : this.clientListeners){
            listener.updateClientList(this.getContext().getClientNameList());
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
