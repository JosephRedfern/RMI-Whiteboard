import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by joe on 16/02/15.
 */
public class ServerApp {
    public static final String SERVER_NAME = "Whiteboard";
    public static final int SERVER_PORT = 1099;

    public static void main(String args[]){
        try{
            WhiteboardServer server = new WhiteboardServer();
            System.out.printf("Server object hash (server-side): %s%n", server.hashCode());
            Registry reg = LocateRegistry.createRegistry(SERVER_PORT);

            System.out.printf("[+] Created local registry%n");
            Naming.rebind(SERVER_NAME, server);

            System.out.printf("[+] Bound to %s%n", SERVER_NAME);

        }catch(Exception e){
            System.err.println("An error has occurred!");
            e.printStackTrace();
        }
    }
}
