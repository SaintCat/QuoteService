package callback;

import java.rmi.*;
import java.rmi.server.*;

import server.Server;
import client.ClientGUI;

public class CallbackImpl extends UnicastRemoteObject implements Callback
{
    private static final long serialVersionUID = 3671520860342032921L;
    private Server c;
    private ClientGUI gui;

    public CallbackImpl(Server c, ClientGUI gui) throws RemoteException {
        this.c = c;
        this.gui = gui;
    }

    public void displayQuote(){
        try {
        	gui.displayQuote(c.getLastQuote());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }  
}