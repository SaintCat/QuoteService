package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import callback.Callback;

public interface Server extends Remote
{
    public void addClient(Callback cb) throws RemoteException;
    public void removeClient(Callback cb) throws RemoteException;
    public String getLastQuote() throws RemoteException;
    public String getRandomQuote() throws RemoteException;
}
