//package security;
//
//import javax.net.ServerSocketFactory;
//import javax.net.ssl.SSLServerSocketFactory;
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.rmi.server.RMIServerSocketFactory;
//
//public class RMISSLServerSocketFactory implements RMIServerSocketFactory {
//
//    private final SSLServerSocketFactory ssf;
//
//    public RMISSLServerSocketFactory() throws IOException {
//        this.ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
//    }
//
//    // can also initialize with specific cipher suites and client authentication requirements
//    public RMISSLServerSocketFactory(String[] cipherSuites, boolean needClientAuth) throws IOException {
//        this.ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
//        this.ssf.setEnabledCipherSuites(cipherSuites);
//        this.ssf.setNeedClientAuth(needClientAuth);
//    }
//
//    @Override
//    public ServerSocket createServerSocket(int port) throws IOException {
//        ServerSocket ss = ssf.createServerSocket(port);
//        System.out.println("SSL Server Socket created on port: " + port);
//        return ss;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        return (obj != null && obj.getClass().equals(this.getClass()));
//    }
//
//    @Override
//    public int hashCode() {
//        return this.getClass().hashCode();
//    }
//}