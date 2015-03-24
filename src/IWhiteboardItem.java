import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

/**
 * Created by joe on 16/02/15.
 */
public interface IWhiteboardItem extends Remote{
    public Shape getShape() throws RemoteException;
    public Date getCreationTime() throws RemoteException;
    public IWhiteboardClient getOwner() throws RemoteException;
    public Color getColour() throws RemoteException;
}
