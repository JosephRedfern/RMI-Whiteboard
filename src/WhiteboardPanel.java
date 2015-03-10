import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by joe on 27/02/15.
 */
public class WhiteboardPanel extends JPanel{
    ArrayList<IWhiteboardItem> shapes;

    public WhiteboardPanel(ArrayList<IWhiteboardItem> shapes){
        this.shapes = shapes;
        this.setBackground(new Color(255, 0, 0));
   }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        for(IWhiteboardItem item : this.shapes){
            try {
                g2.draw(item.getShape());
            }catch(RemoteException e){
                e.printStackTrace();
            }
        }
    }

    public void addItem(IWhiteboardItem item){
        this.shapes.add(item);
        this.repaint();
    }


}
