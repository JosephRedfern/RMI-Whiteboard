import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by joe on 14/02/15.
 */
public interface IWhiteboardServer extends Remote{
    public ICommunicationContext register(IWhiteboardClient client) throws RemoteException;
}
