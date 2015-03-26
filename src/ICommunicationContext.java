import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by joe on 14/02/15.
 */
public interface ICommunicationContext extends Remote{
    public void sendMessage(String s) throws RemoteException;
    public void addShape(Shape shape, Color colour) throws RemoteException;
    public ArrayList<IWhiteboardItem> getShapes() throws RemoteException;
    public void clearShapes() throws RemoteException;
    public void clearAllShapes() throws RemoteException;
    public ArrayList<String> getClientNameList() throws RemoteException;
    public void notifyOfClientNameChange() throws RemoteException;
}
