import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

/**
 * Created by joe on 16/02/15.
 */
public class WhiteboardItem extends UnicastRemoteObject implements IWhiteboardItem{
    private Shape shape;
    private IWhiteboardClient client;
    private Date creationDate;

    public WhiteboardItem(IWhiteboardClient client, Shape shape) throws RemoteException{
        this.client = client;
        this.shape = shape;
        this.creationDate = new Date();
    }

    public Date getCreationTime(){
        return this.creationDate;
    }

    public IWhiteboardClient getOwner(){
        return this.client;
    }

    public Shape getShape(){
        return this.shape;
    }
}
