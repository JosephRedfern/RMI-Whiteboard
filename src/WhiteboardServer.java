/**
 * Created by joe on 14/02/15.
 */

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Iterator;

public class WhiteboardServer extends UnicastRemoteObject implements IWhiteboardServer, Serializable{
    private WhiteboardClientManager clientManager;
    private ArrayList<IWhiteboardItem> shapes;

    public WhiteboardServer() throws RemoteException{
        super();
        this.clientManager = new WhiteboardClientManager(this);
        this.shapes = new ArrayList<IWhiteboardItem>();
    }

    public ICommunicationContext register(IWhiteboardClient c) throws IllegalStateException, RemoteException{
        if(this.clientManager.contains(c))
            throw new IllegalStateException(String.format("Client %s has already been registered", c.toString()));

        this.clientManager.addClient(c);

        ICommunicationContext clientContext = new CommunicationContext(c, this);
        System.out.printf("[+] Registered client %s%n", c.toString());
        printClients();

        return clientContext;
    }

    /***
     * Prints all active clients.
     * @throws RemoteException
     */
    public void printClients() throws RemoteException{
        if(this.clientManager.clientCount()>0){
            System.out.printf("%d active clients:%n%n", this.clientManager.clientCount());
            for (IWhiteboardClient client : this.clientManager) {
                System.out.printf("Client:\t%s%n", client.getName());
            }
        }else{
            System.out.println("No active clients.");
        }
    }

    /***
     * Given an IWhiteboardClient client, and a java.awt.Shape shape, create an IWhiteboardItem, store it, and
     * distribute it to all clients.
     *
     * @param source The source client
     * @param shape The shape to be distributed
     * @throws RemoteException
     */
    public void addShape(IWhiteboardClient source, Shape shape, Color colour) throws RemoteException{
        IWhiteboardItem shapeItem = new WhiteboardItem(source, shape, colour);
        this.shapes.add(shapeItem);
        for(IWhiteboardClient client : this.clientManager){
            client.retrieveShape(shapeItem);
        }
    }

    /***
     * Remove all IWhiteboardItems belonging to a particular client.
     * @param client
     * @throws RemoteException
     */
    public void removeItemsByClient(IWhiteboardClient client) throws RemoteException{
        for (Iterator<IWhiteboardItem> iterator =  this.shapes.iterator(); iterator.hasNext();){
            IWhiteboardItem currentItem = iterator.next();
            if(currentItem.getOwner() == client){
                iterator.remove();
            }
        }

        globalShapeResync(); //resyncShapes all clients
    }

    public void clearAllShapes(IWhiteboardClient requestor){
        try {
            System.out.printf("[!] Removing All Items, as requested by %s%n", requestor.getName());
            this.shapes = new ArrayList<IWhiteboardItem>();
            globalShapeResync();
        }catch(RemoteException err){
            System.err.printf("[!] Error removing all items%n");
            err.printStackTrace();
        }
    }

    private void globalShapeResync(){
        for(IWhiteboardClient client : clientManager){
            try {
                client.resyncShapes();
            }catch(RemoteException err){
                err.printStackTrace();
            }
        }
    }

    public void globalClientNameListResync(){
        for(IWhiteboardClient client : clientManager){
            try{
                client.resyncClientNameList();
            }catch(RemoteException err){
                err.printStackTrace();
            }
        }
    }

    /***
     * Returns all shapes associated with the server
     * @return ArrayList of IWhiteboardItems on the server.
     */
    public ArrayList<IWhiteboardItem> getShapes(){
        return this.shapes;
    }

    /***
     * Get a list of all connected clients, by name.
     * @return ArrayList of client friendly names.
     * @throws RemoteException
     */
    public ArrayList<String> getClientNameList() throws RemoteException{
        ArrayList<String> clientNames = new ArrayList<String>();
        for(IWhiteboardClient client : this.clientManager){
            clientNames.add(client.getName());
        }

        return clientNames;
    }
}
