import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Field;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by joe on 10/03/15.
 */
public class WhiteboardClientGUI implements IWhiteboardItemListener, IWhiteboardClientListener{

    IWhiteboardClient client;
    private JPanel panel1;
    private JPanel boardContainer;
    private JButton getShapes;
    private JPanel buttonContainer;
    private JButton clearMineButton;
    private JPanel controlContainer;
    private JComboBox colourSelector;
    private JComboBox shapeSelector;
    private JSpinner heightSpinner;
    private JSpinner widthSpinner;
    private JList clientList;
    private JTextField clientNameField;
    private JButton setNameButton;
    private JButton clearAllButton;
    private WhiteboardPanel panel;
    private HashMap<String, Color> colourMap = new HashMap<String, Color>();
    private HashSet<String> possibleShapes = new HashSet<String>();

    public WhiteboardClientGUI(IWhiteboardClient client) throws RemoteException{
        JFrame frame = new JFrame();
        this.client = client;
        client.addItemListener(this);
        client.addClientListener(this);

        this.initColourMap();
        this.initShapeList();
        this.updateClientList(client.getContext().getClientNameList());

        this.widthSpinner.setValue(50);
        this.heightSpinner.setValue(50);


        for(String colourName : this.colourMap.keySet()){
           this.colourSelector.addItem(colourName);
        }

        panel = new WhiteboardPanel(client.getContext().getShapes());
        panel.addMouseListener(new MouseAdapter() {
            @Override
            @SuppressWarnings("Unchecked")
            public void mousePressed(MouseEvent e) {
                try {
                    WhiteboardClientGUI.this.client.getContext().addShape(ShapeFactory.getShape((ShapeFactory.Shape)WhiteboardClientGUI.this.shapeSelector.getSelectedItem(), e.getX(), e.getY(), (Integer)WhiteboardClientGUI.this.widthSpinner.getValue(), (Integer)WhiteboardClientGUI.this.heightSpinner.getValue()), WhiteboardClientGUI.this.getCurrentColour());
                }catch(RemoteException err){
                    err.printStackTrace();
                }
            }
        });

        boardContainer.add(panel);

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


        frame.add(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(640, 480);
        setNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    WhiteboardClientGUI.this.client.setName(WhiteboardClientGUI.this.clientNameField.getText());
                }catch(RemoteException err){
                    System.err.println("[-] Error setting name, remote exception occured. ");
                }
            }
        });
        clearAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    WhiteboardClientGUI.this.client.getContext().clearAllShapes();
                }catch(RemoteException err){
                    System.out.printf("[-] Error clearing shapes, remote exception occured. ");
                }
            }
        });
    }

    private Color getCurrentColour(){
        return this.colourMap.get(this.colourSelector.getSelectedItem());
    }

    private void initColourMap(){
        try {
            Class colours = Class.forName("java.awt.Color");

            for(Field colour : colours.getFields()) {
                if(colour.getDeclaringClass().equals(java.awt.Color.class)) {
                    if(Character.isUpperCase(colour.getName().charAt(0))) {
                        char[] colNameLower = colour.getName().toLowerCase().toCharArray();
                        colNameLower[0] = Character.toUpperCase(colNameLower[0]);
                        colourMap.put(String.valueOf(colNameLower).replace("_", " "), (Color) colour.get(null));
                    }
                }
            }

        }catch(ClassNotFoundException cnfErr){
            System.err.println("Error!");
        }
        catch(IllegalAccessException iaeErr){

        }
    }

    private void initShapeList() {
        for (ShapeFactory.Shape s : ShapeFactory.Shape.values()) {
            this.shapeSelector.addItem(s);
        }
    }

    private void getShape(){

    }

    public void receiveShape(IWhiteboardItem item){
        panel.addItem(item);
    }

    public void updateClientList(ArrayList<String> clientNames){
        DefaultComboBoxModel clientModel = new DefaultComboBoxModel();
        for(String name : clientNames){
            clientModel.addElement(name);
        }
        this.clientList.setModel(clientModel);
    }

    public void resyncShapes(){
        try {
            panel.updateShapes(this.client.getContext().getShapes());
        }catch(RemoteException err){
            err.printStackTrace();
        }
    }

}
