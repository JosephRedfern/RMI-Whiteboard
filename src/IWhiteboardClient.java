import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by joe on 15/02/15.
 */
public interface IWhiteboardClient extends Remote {
    public void sendMessage(String message) throws RemoteException;
}
