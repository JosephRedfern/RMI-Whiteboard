import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by joe on 20/02/15.
 */
public class WhiteboardClientManager implements Iterable<IWhiteboardClient>{
    private Set<IWhiteboardClient> clients;


    public WhiteboardClientManager(){
        //Use a map-ified ConcurrentHashMap, so that we can be thread safe.
        this.clients = Collections.newSetFromMap(new ConcurrentHashMap<IWhiteboardClient, Boolean>());
        new Thread(new ClientWatchdog(this)).start();
    }

    /***
     * Registers a client with the WhiteboardClientManager
     * @param client - the client to add to the collection
     */
    public void addClient(IWhiteboardClient client){
        this.clients.add(client);
    }

    /***
     * Checks if the given client object exists in the WhiteboardClientManager
     * @param client
     * @return
     */
    public boolean contains(IWhiteboardClient client){
        return this.clients.contains(client);
    }

    /***
     * Get the number of active clients.
     * @return client count
     */
    public int clientCount(){
        return this.clients.size();
    }

    /***
     * Removes a given IWhiteboardClient from the client manager
     * @param client
     */
    public void removeClient(IWhiteboardClient client){
        this.clients.remove(client);
    }

    public Iterator<IWhiteboardClient> iterator(){
        return clients.iterator();
    }

    /***
     * The ClientWatchdog is responsible for 'pinging' each client, and de-registering them if no response is recieved.
     *
     * This is done in a separate thread to avoid slowdowns.
     */
    class ClientWatchdog implements Runnable{
        WhiteboardClientManager manager;

        public ClientWatchdog(WhiteboardClientManager manager){
            this.manager = manager;
        }

        public void run(){
            //We're in a separate thread, so an infinite loop is OK.
            while(true) {
                if(manager.clientCount() > 0) {
                    System.out.println("[+] Pinging Clients:");
                    for (Iterator<IWhiteboardClient> iterator =  this.manager.iterator(); iterator.hasNext();) {
                        IWhiteboardClient client = iterator.next();
                        try {
                            client.pingClient();
                        } catch (RemoteException e) { //RemoteException is thrown when we can't talk to the client.
                            System.err.println("[-] Lost client, removing...");
                            manager.removeClient(client);
                        }
                    }
                }else{
                    System.out.println("[+] No clients connected");
                }
                try {
                    Thread.sleep(1 * 1000); // Ping every 1 second
                } catch (InterruptedException e) {
                    continue; //We're not really that bothered if we're interrupted.
                }
            }
        }
    }
}
