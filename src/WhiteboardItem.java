import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

/**
 * This class contains a shape, a shape owner and a shape creation date.
 * Created by joe on 16/02/15.
 */
public class WhiteboardItem extends UnicastRemoteObject implements IWhiteboardItem{
    private Shape shape;
    private IWhiteboardClient client;
    private Date creationDate;
    private Color colour;

    public WhiteboardItem(IWhiteboardClient client, Shape shape, Color colour) throws RemoteException{
        this.client = client;
        this.shape = shape;
        this.creationDate = new Date();
        this.colour = colour;
    }

    public Color getColour(){
        return this.colour;
    }

    /***
     * Gets the time/date that the shape was created on.
     * @return The Date object representing the date/time this IWhiteboardItem was created on.
     */
    public Date getCreationTime(){
        return this.creationDate;
    }

    /***
     * Returns the IWhiteboardClient object associated with this WhiteboardItem.
     * @return
     */
    public IWhiteboardClient getOwner(){
        return this.client;
    }

    /***
     * Get the
     * @return the shape associated with this WhiteboardItem
     */
    public Shape getShape(){
        return this.shape;
    }
}
