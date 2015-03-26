import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;

/**
 * Created by joe on 14/02/15.
 */
public interface IWhiteboardServer extends Remote{
    public ICommunicationContext register(IWhiteboardClient client) throws RemoteException;
    public void addShape(IWhiteboardClient client, Shape shape, Color colour) throws RemoteException;
    public ArrayList<IWhiteboardItem> getShapes() throws RemoteException;
    public void removeItemsByClient(IWhiteboardClient client) throws RemoteException;
    public ArrayList<String> getClientNameList() throws RemoteException;
    public void globalClientNameListResync() throws RemoteException;
    public void clearAllShapes(IWhiteboardClient requester) throws RemoteException;
}
