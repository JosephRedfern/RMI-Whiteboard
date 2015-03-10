/**
 * Created by joe on 14/02/15.
 */

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class WhiteboardServer extends UnicastRemoteObject implements IWhiteboardServer, Serializable{
    private WhiteboardClientManager clientManager;
    private ArrayList<IWhiteboardItem> shapes;

    public WhiteboardServer() throws RemoteException{
        super();
        this.clientManager = new WhiteboardClientManager();
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

    public void addShape(IWhiteboardClient source, Shape shape) throws RemoteException{
        IWhiteboardItem shapeItem = new WhiteboardItem(source, shape);
        this.shapes.add(shapeItem);
        for(IWhiteboardClient client : this.clientManager){
            client.retrieveShape(shapeItem);
        }
    }

    public ArrayList<IWhiteboardItem> getShapes(){
        return this.shapes;
    }
}
