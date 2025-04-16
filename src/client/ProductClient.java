package client;

import rmi.ProductInterface;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import model.AuthToken;
import model.Products;

public class ProductClient extends ClientManager<ProductInterface> {

    private final String bindObjectName = "/ProductService";
    private AuthToken token;

    public ProductClient(AuthToken token) {
        this.token = token;
    }

    @Override
    protected String getBindObject() {
        return this.bindObjectName;
    }

    public List<Products> fetchAllProduct() {
        try {
            return connectToServer().getAllProducts(token);
        } catch (MalformedURLException | NotBoundException | RemoteException ex) {
            System.err.println("Exception in fetchAllProduct: " + ex.getMessage());
            return null;
        }
    }

    public List<Products> fetchNewComerProduct() {
        try {
            return connectToServer().getNewcomerProducts(token, 9);
        } catch (RemoteException | NotBoundException | MalformedURLException ex) {
            return null;
        }
    }

    public List<Products> fetchBestSellerProduct() {
        try {
            return connectToServer().getBestSellerProducts(token, 9);
        } catch (RemoteException | NotBoundException | MalformedURLException ex) {
            return null;
        }
    }

}
