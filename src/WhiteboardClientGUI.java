import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;

/**
 * Created by joe on 10/03/15.
 */
public class WhiteboardClientGUI implements IWhiteboardItemListener{

    IWhiteboardClient client;
    private JPanel panel1;
    private JPanel boardContainer;
    private JButton getShapes;
    private JPanel buttonContainer;
    private JButton clearMineButton;
    private WhiteboardPanel panel;

    public WhiteboardClientGUI(IWhiteboardClient client) throws RemoteException{
        JFrame frame = new JFrame();
        this.client = client;
        client.addItemListener(this);

        getShapes.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e){
                                                WhiteboardClientGUI.this.resync();
                                            }
                                        });

        panel = new WhiteboardPanel(client.getContext().getShapes());
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    WhiteboardClientGUI.this.client.getContext().addShape(new Rectangle(e.getX(), e.getY(), 50, 50), new Color(255, 255, 255));
                }catch(RemoteException err){
                    err.printStackTrace();
                }
            }
        });

        boardContainer.add(panel);

        frame.add(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(200, 200);
        clearMineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    WhiteboardClientGUI.this.client.getContext().clearShapes();
                }catch(RemoteException err){
                    err.printStackTrace();
                }
            }
        });
    }

    public void receiveShape(IWhiteboardItem item){
        panel.addItem(item);
        System.out.println("~~~~~~~~~~~~> GOT SHAPE (in WhiteboardClientGUI)!!!");
    }

    public void resync(){
        try {
            panel.updateShapes(this.client.getContext().getShapes());
        }catch(RemoteException err){
            err.printStackTrace();
        }
    }
}
