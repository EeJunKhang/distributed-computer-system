package client;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import security.RMISSLClientSocketFactory;
import utils.ConfigReader;

public abstract class ClientManager<T extends Remote> {

    private static final String serverIP = ConfigReader.getServerIP();
    private static final int rmiPort = ConfigReader.getRmiPort();

    protected T connectToServer() throws RemoteException, NotBoundException, MalformedURLException, UnknownHostException {
        String bindObject = getBindObject();
        Registry registry = LocateRegistry.getRegistry(
                serverIP, rmiPort,
                new RMISSLClientSocketFactory());
        
        T service = (T) registry.lookup(bindObject);
        return service;
    }

    protected abstract String getBindObject();
}
