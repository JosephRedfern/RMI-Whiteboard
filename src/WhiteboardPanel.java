import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by joe on 27/02/15.
 */
public class WhiteboardPanel extends JPanel{
    ArrayList<IWhiteboardItem> shapes;

    public WhiteboardPanel(ArrayList<IWhiteboardItem> shapes){
        this.shapes = shapes;
        Random r = new Random();
        this.setBackground(new Color(r.nextFloat(), r.nextFloat(), r.nextFloat()));
   }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        for(IWhiteboardItem item : this.shapes){
            try {
                System.out.printf("Drawing shape by %s at %s%n", item.getOwner(), item.getCreationTime());
                g2.setColor(item.getColour());
                g2.fill(item.getShape());
            }catch(RemoteException e){
                e.printStackTrace();
            }
        }
    }

    public void addItem(IWhiteboardItem item){
        this.shapes.add(item);
        this.repaint();
    }

    public void updateShapes(ArrayList<IWhiteboardItem> shapes){
        System.out.printf("[i] Updating Shapes%n");
        this.shapes = shapes;
        this.repaint();
    }

}
