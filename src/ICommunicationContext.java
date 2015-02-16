import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by joe on 14/02/15.
 */
public interface ICommunicationContext extends Remote{
    public void sendMessage(String s) throws RemoteException;
}
