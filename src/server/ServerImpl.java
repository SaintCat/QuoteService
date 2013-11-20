package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;

import callback.Callback;

public class ServerImpl extends UnicastRemoteObject implements Server{

	private static final long serialVersionUID = 1L;
	
	private static final String fileName = "quotes.txt";
	private static final String encodingFont = "UTF-8";
	private static ArrayList<String> quotesList;
	private static ArrayList<Callback> clients;
	private static String lastQuote;
	private static int intervalBetweenUpdatesInMilles = 3000;
	private static Random rand = new Random();
	
	public static void main( String[] args ) {
		try {
			Server server =  new ServerImpl();
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			Naming.rebind("DRAWING-SERVER", server);
			System.out.print("Server started successfully");
		} catch (java.net.MalformedURLException e) {
			System.err.println("Malformed URL Server name " + e.toString());
			System.exit(1);
		} catch (RemoteException e) {
			System.err.println("General Communication Error: " + e.toString());
			System.exit(1);
		}	
		
		updateLastQuote();
		
		Thread t = new Thread(){
			@Override
			public void run() {
				try {
					while (true) {
						if (!clients.isEmpty()) {
							updateLastQuote();
							distributeUpdate();
						}
						sleep(intervalBetweenUpdatesInMilles);
					}
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		};
		t.setDaemon(true);
		t.start();
	}
	
	private static void updateLastQuote(){
		lastQuote = quotesList.get(getRandomInteger());
	}
	
	private static int getRandomInteger(){
		int maxQuotes = quotesList.size();
		return rand.nextInt(maxQuotes);
	}
	
	public ServerImpl() throws RemoteException{
		quotesList = new ArrayList<String>();
		clients = new ArrayList<Callback>();
		readFile();
	}
 
	private static void readFile() {
		File file = new File(fileName);

		BufferedReader br;
		String line = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), encodingFont));
			while ((line = br.readLine()) != null) {
				quotesList.add(line);
			}
			br.close();
		} catch(FileNotFoundException ex){
			ex.printStackTrace();
		} catch(IOException ex){
			ex.printStackTrace();
		}
	
		
	}
	

	private static void distributeUpdate() {
		for (Callback client : clients) {
			try {
				client.displayQuote();
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void addClient(Callback cb) throws RemoteException {
		synchronized(clients) {
    		clients.add(cb);
    		cb.displayQuote();
    	}
		
	}

	@Override
	public void removeClient(Callback cb) throws RemoteException {
		synchronized(clients)
    	{
    		clients.remove(cb); 
    	}
		
	}

	@Override
	public String getLastQuote() throws RemoteException {	
		return lastQuote;
	}
	
	@Override
	public String getRandomQuote() throws RemoteException {	
		return quotesList.get(getRandomInteger());
	}
}
