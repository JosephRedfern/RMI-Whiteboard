import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by joe on 15/02/15.
 */
public interface IWhiteboardClient extends Remote {
    public void sendMessage(String message) throws RemoteException;
    public String getName() throws RemoteException;
    public void setName(String name) throws RemoteException;
    public double pingClient() throws RemoteException;
    public void retrieveShape(IWhiteboardItem shape) throws RemoteException;
    public ICommunicationContext  getContext() throws RemoteException;
}
