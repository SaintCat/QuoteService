package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import server.Server;
import callback.CallbackImpl;

public class Client {
    private CallbackImpl returnComms;
    private Server serverObject;
    private ClientGUI gui;

    public Client(ClientGUI gui) {
    		this.gui=gui;
    		buildClient();
    }
    
    private void buildClient(){
        try{
        	Registry registry=LocateRegistry.getRegistry("localhost", 1099 );
            serverObject=(Server)(registry.lookup("DRAWING-SERVER"));
            returnComms = new CallbackImpl(serverObject, this.gui);
            //addClient();
        }
        catch(java.rmi.ConnectException e) {
            System.out.println("FATAL ERROR: Could not connect to server.");
            System.exit(0);
        }
        catch (Exception e) {
            System.out.println("General error: " + e.toString());
            System.exit(0);
        }
    }


    public void removeClient() throws RemoteException {
        serverObject.removeClient(returnComms);
    }

    public void addClient() throws RemoteException {
        serverObject.addClient(returnComms);
    }
    
    public String getLastQuote() {
    	String randomQuote = null;
    	try {
			randomQuote = serverObject.getRandomQuote();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	return randomQuote;
    }
}