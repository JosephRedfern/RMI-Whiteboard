import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * Created by joe on 11/03/15.
 */
public interface IWhiteboardItemListener {
    public void receiveShape(IWhiteboardItem shape);
    public void resyncShapes();
}
