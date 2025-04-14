package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import utils.ConfigReader;

public abstract class ClientManager<T extends Remote> {

    private static final String serverIP = ConfigReader.getServerIP();
    private static final int rmiPort = ConfigReader.getRmiPort();

    protected T connectToServer() throws RemoteException, NotBoundException, MalformedURLException {
        String bindObject = getBindObject();
        T service = (T) Naming.lookup("rmi://" + serverIP + ":" + rmiPort + bindObject);
        return service;
    }

    protected abstract String getBindObject();
}
