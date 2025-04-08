package security;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;

public class RMISSLClientSocketFactory implements RMIClientSocketFactory {

    private final SSLSocketFactory sf;

    public RMISSLClientSocketFactory() throws IOException {
        this.sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
    }

    // can also initialize with specific cipher suites
    public RMISSLClientSocketFactory(String[] cipherSuites) throws IOException {
        this.sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
        this.sf.setEnabledCipherSuites(cipherSuites);
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        Socket s = sf.createSocket(host, port);
        System.out.println("SSL Client Socket created for host: " + host + " port: " + port);
        return s;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null && obj.getClass().equals(this.getClass()));
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}