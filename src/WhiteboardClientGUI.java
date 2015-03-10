import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Random;

/**
 * Created by joe on 20/02/15.
 */
public class WhiteboardClientGUI {

    private IWhiteboardClient client;
    private JTextField nameField;
    private JButton nameApplyField;
    private JPanel container;
    private JButton randomRectangleButton;
    private JPanel panelHolder;
    private WhiteboardPanel whiteboardPanel;

    public WhiteboardClientGUI(IWhiteboardClient client) throws RemoteException {

        JFrame frame = new JFrame();
        frame.add(container);
        frame.setVisible(true);
        frame.setSize(500, 500);

        this.client = client;

        this.whiteboardPanel = new WhiteboardPanel(this.client.getContext().getShapes());
        container.add(this.whiteboardPanel);
        //this.panelHolder.add(wbPanel);

        this.nameApplyField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    WhiteboardClientGUI.this.client.setName(WhiteboardClientGUI.this.nameField.getText());
                } catch (RemoteException err) {
                    System.err.println("[!] Error talking to remote server");
                }
            }
        });

        this.randomRectangleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Shape shape = new Rectangle((int) Math.floor(Math.random() * WhiteboardClientGUI.this.whiteboardPanel.getHeight()), (int) Math.floor(Math.random() * WhiteboardClientGUI.this.whiteboardPanel.getWidth()), (int) Math.floor(Math.random() * WhiteboardClientGUI.this.whiteboardPanel.getHeight()), (int) Math.floor(Math.random() * WhiteboardClientGUI.this.whiteboardPanel.getWidth()));
                try {
                    WhiteboardClientGUI.this.client.getContext().addShape(shape);
                } catch (RemoteException err) {
                    System.err.println("Couldn't comm with remote server");
                }
            }
        });

    }
}
