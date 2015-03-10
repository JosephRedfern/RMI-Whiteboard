import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by joe on 20/02/15.
 */
public class WhiteboardClientManager implements Iterable<IWhiteboardClient>{
    private HashSet<IWhiteboardClient> clients;

    public WhiteboardClientManager(){
        this.clients = new HashSet<IWhiteboardClient>();
        new Thread(new ClientWatchdog(this)).start();
    }

    public void addClient(IWhiteboardClient client){
        this.clients.add(client);
    }

    public boolean contains(IWhiteboardClient client){
        return this.clients.contains(client);
    }

    public int clientCount(){
        return this.clients.size();
    }

    public void removeClient(IWhiteboardClient client){
        this.clients.remove(client);
    }

    public Iterator<IWhiteboardClient> iterator(){
        return clients.iterator();
    }

    class ClientWatchdog implements Runnable{
        WhiteboardClientManager manager;

        public ClientWatchdog(WhiteboardClientManager manager){
            this.manager = manager;
        }

        public void run(){
            System.out.println("[+] Starting Client Watchdog");
            while(true) {
                if(manager.clientCount() > 0) {
                    System.out.println("[+] Pinging Clients:");
                    for (Iterator<IWhiteboardClient> iterator =  this.manager.iterator(); iterator.hasNext();) {
                        IWhiteboardClient client = iterator.next();
                        try {
                            client.pingClient();
                            System.out.printf("\t[+] Client %s is online! %n", client.getName());
                        } catch (RemoteException e) {
                            System.err.println("[-] Lost client, removing...");
                            manager.removeClient(client);
                        }
                    }
                }else{
                    System.out.println("[+] No clients connected");
                }
                try {
                    Thread.sleep(1 * 1000); // 1 seconds.
                } catch (InterruptedException e) {
                    continue; //not that fussed
                }
            }
        }
    }
}
